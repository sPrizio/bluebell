package com.bluebell.anther.engine.impl;

import com.bluebell.anther.engine.DecisionEngine;
import com.bluebell.anther.models.engine.Decision;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.CumulativeStrategyReportEntry;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.services.reporting.impl.BloomReportingService;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.anther.util.DirectoryUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

/**
 * Implementation of {@link DecisionEngine} specific to {@link Bloom}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class BloomDecisionEngine implements DecisionEngine<Bloom, BloomStrategyParameters> {

    private int window = 1;


    //  METHODS

    @Override
    public Decision<BloomStrategyParameters> decide(final SimulationResult<BloomStrategyParameters> simulationResult) {
        List<Decision<BloomStrategyParameters>> decisions = consider(simulationResult);
        return decisions.get(decisions.size() - 1);
    }

    @Override
    public List<Decision<BloomStrategyParameters>> consider(final SimulationResult<BloomStrategyParameters> simulationResult) {

        final List<Pair<CumulativeData, Integer>> decisions = new ArrayList<>();
        final List<CumulativeData> data = getData(simulationResult);
        int limit = CollectionUtils.isNotEmpty(data) ? data.get(0).entries().size() : 0;

        int windowCounter = 1;

        decisions.add(Pair.with(data.get(30), 0));
        CumulativeData currentDecision = data.get(0);
        CumulativeData currentMax = data.get(0);

        for (int i = 0; i < limit; i++) {
            if (windowCounter == this.window) {
                double currentScore = 0.0;
                double maxScore = 0.0;

                //  get the current score
                for (final CumulativeData file : data) {
                    // get the current score
                    if (file.equals(currentDecision)) {
                        currentScore = file.entries().get(i).points();
                    }
                }

                //  find the max score
                for (final CumulativeData file : data) {
                    if (file.entries().get(i).points() > maxScore) {
                        maxScore = file.entries().get(i).points();
                        currentMax = file;
                    }
                }

                if (maxScore > currentScore && !currentMax.equals(currentDecision)) {
                    currentDecision = currentMax;
                    decisions.add(Pair.with(currentDecision, currentDecision.entries().get(i).trades()));
                }

                windowCounter = 0;
            }

            windowCounter += 1;
        }

        return decisions.stream().map(dec -> getParametersForFile(dec, simulationResult)).map(pair -> new Decision<>(pair.getValue0(), pair.getValue1())).sorted(Comparator.comparing(Decision::index)).toList();
    }

    /**
     * Evaluates a series of decisions
     *
     * @param simulationResult {@link SimulationResult}
     * @return {@link List} of {@link Pair}
     */
    public List<Pair<Integer, Double>> evaluate(final SimulationResult<BloomStrategyParameters> simulationResult) {

        final List<Pair<Integer, Double>> res = new ArrayList<>();
        while (this.window < 2232) {
            System.out.println("Deciding for: " + this.window);
            final Map<Decision<BloomStrategyParameters>, List<Pair<Integer, Integer>>> map = getTradeIntervalsForDecisions(consider(simulationResult), simulationResult);

            final List<CumulativeStrategyReportEntry> entries = new ArrayList<>();
            map.forEach((key, value) -> entries.addAll(getReportEntriesForDecision(key, value, simulationResult)));
            final double value = entries.stream().mapToDouble(CumulativeStrategyReportEntry::pointsForTrade).sum();

            res.add(Pair.with(this.window, value));
            this.window += 1;
        }

        return res;
    }


    //  HELPERS

    /**
     * Obtains the cumulative data from the output directories
     *
     * @param simulationResult {@link SimulationResult}
     * @return {@link List} of {@link CumulativeData}
     */
    private List<CumulativeData> getData(final SimulationResult<BloomStrategyParameters> simulationResult) {

        final List<CumulativeData> data = loadDataFromDirectory();
        if (CollectionUtils.isNotEmpty(data)) {
            return data;
        } else {
            final BloomReportingService strategyReportingService = new BloomReportingService();
            strategyReportingService.generateCumulativeReport(simulationResult);
        }

        return loadDataFromDirectory();
    }

    /**
     * Reads the given {@link File} and collects the entries in to al ist
     *
     * @param file {@link File}
     * @return {@link List} of {@link CumulativeStrategyReportEntry}
     */
    private List<CumulativeStrategyReportEntry> loadEntriesFromFile(final File file) {

        final List<CumulativeStrategyReportEntry> entries = new ArrayList<>();
        try {
            final List<String> allLines = Files.readAllLines(file.toPath());
            for (final String line : allLines) {
                final String[] split = line.split("\t");
                if (split.length < 6) {
                    continue;
                }

                entries.add(
                        new CumulativeStrategyReportEntry(
                                Double.parseDouble(split[0].trim()),
                                Double.parseDouble(split[1].trim().replace("$", StringUtils.EMPTY)),
                                Integer.parseInt(split[2].trim()),
                                StringUtils.capitalize(split[2].trim().toLowerCase()),
                                LocalDateTime.parse(split[3].trim(), DateTimeFormatter.ofPattern("MMM dd yyy 'at' HH:mm:ss")),
                                LocalDateTime.parse(split[4].trim(), DateTimeFormatter.ofPattern("MMM dd yyy 'at' HH:mm:ss")),
                                Double.parseDouble(split[5].trim()),
                                Double.parseDouble(split[6].trim().replace("$", StringUtils.EMPTY))
                        )
                );
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Loads the data from the output directories
     *
     * @return {@link List} of {@link CumulativeData}
     */
    private List<CumulativeData> loadDataFromDirectory() {

        // look for files. if the directory exists and is not empty, read from it
        final File directory = new File(DirectoryUtil.getDirectory("cumulative/bloom"));
        try (Stream<Path> paths = Files.list(directory.toPath())) {
            final List<Path> list = paths.toList();

            if (CollectionUtils.isNotEmpty(list)) {
                final List<CumulativeData> data = new ArrayList<>();
                list.forEach(path -> {
                    final File file = path.toFile();
                    final String[] split = file.getName().replace("cumulative-", StringUtils.EMPTY).replace(".txt", StringUtils.EMPTY).split("_");
                    final Triplet<Integer, Integer, Double> triplet = Triplet.with(Integer.parseInt(split[0].substring(0, 2)), Integer.parseInt(split[0].substring(2)), Double.parseDouble(split[1]));

                    data.add(new CumulativeData(triplet.getValue0(), triplet.getValue1(), triplet.getValue2(), loadEntriesFromFile(file)));
                });

                return data.stream().sorted(Comparator.comparing(CumulativeData::startHour).thenComparing(CumulativeData::startMinute).thenComparing(CumulativeData::variance)).toList();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return List.of();
    }

    /**
     * Obtains the matching {@link BloomStrategyParameters} for the {@link CumulativeData} since the data files are generated from strategy parameters
     *
     * @param data {@link Pair} of {@link CumulativeData} and integers
     * @param simulationResult {@link SimulationResult}
     * @return {@link Pair} of {@link BloomStrategyParameters} and integers
     */
    private Pair<BloomStrategyParameters, Integer> getParametersForFile(final Pair<CumulativeData, Integer> data, final SimulationResult<BloomStrategyParameters> simulationResult) {
        return
                Pair.with(simulationResult.result()
                        .values()
                        .stream()
                        .flatMap(List::stream)
                        .map(StrategyResult::getStrategyParameters)
                        .filter(sp -> sp.getStartHour() == data.getValue0().startHour())
                        .filter(sp -> sp.getStartMinute() == data.getValue0().startMinute())
                        .filter(sp -> sp.getVariance() == data.getValue0().variance())
                        .findFirst()
                        .orElseThrow(() -> new UnsupportedOperationException("Impossible error")), data.getValue1());
    }

    /**
     * Computes the max size of trades taken across all simulation results (disregarding repetitions)
     *
     * @param simulationResult {@link SimulationResult}
     * @return trade size
     */
    private int getTradesSize(final SimulationResult<BloomStrategyParameters> simulationResult) {

        final Iterator<Map.Entry<LocalDate, List<StrategyResult<BloomStrategyParameters>>>> iterator = simulationResult.result().entrySet().iterator();
        final Map<LocalDate, Integer> map = new HashMap<>();

        while (iterator.hasNext()) {
            Map.Entry<LocalDate, List<StrategyResult<BloomStrategyParameters>>> entry = iterator.next();
            map.put(entry.getKey(), entry.getValue().get(0).getTrades().size());
        }

        return map.values().stream().mapToInt(i -> i).sum();
    }

    /**
     * Computes the trade intervals for each decision. This means that each decision was appropriate for a certain number of trades. This method will capture the intervals of trades
     * where the decision was operating and store their strategy parameters at the time that the trades were taken
     *
     * @param decisions {@link List} of {@link Decision}s
     * @param simulationResult {@link SimulationResult}
     * @return {@link Map} of {@link BloomStrategyParameters}
     */
    private Map<Decision<BloomStrategyParameters>, List<Pair<Integer, Integer>>> getTradeIntervalsForDecisions(final List<Decision<BloomStrategyParameters>> decisions, final SimulationResult<BloomStrategyParameters> simulationResult) {

        final Map<Decision<BloomStrategyParameters>, List<Pair<Integer, Integer>>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(decisions)) {
            for (int i = 0; i < decisions.size() - 1; i++) {
                final Decision<BloomStrategyParameters> decision = decisions.get(i);
                final Decision<BloomStrategyParameters> nextDecision = decisions.get(i + 1);

                List<Pair<Integer, Integer>> pairs = new ArrayList<>();
                if (map.containsKey(decision)) {
                    pairs = new ArrayList<>(map.get(decision));
                }

                pairs.add(Pair.with(decision.index(), nextDecision.index()));
                map.put(decision, pairs);
            }

            List<Pair<Integer, Integer>> pairs = new ArrayList<>();
            if (map.containsKey(decisions.get(decisions.size() - 1))) {
                pairs = new ArrayList<>(map.get(decisions.get(decisions.size() - 1)));
            }

            pairs.add(Pair.with(decisions.get(decisions.size() - 1).index(), getTradesSize(simulationResult)));
            map.put(decisions.get(decisions.size() - 1), pairs);
        }

        return map;
    }

    /**
     * Obtains the cumulative entries for the given decision and pairs. A decision can have multiple pairs (here the pairs refer to intervals, like trades #3 -> #15 and #56 -> #87)
     * where we want we to obtain the cumulative entries that match the given pairs
     *
     * @param decision {@link Decision}
     * @param pairs list of pairs
     * @param simulationResult {@link SimulationResult}
     * @return {@link List} of {@link CumulativeStrategyReportEntry}
     */
    private List<CumulativeStrategyReportEntry> getReportEntriesForDecision(final Decision<BloomStrategyParameters> decision, final List<Pair<Integer, Integer>> pairs, final SimulationResult<BloomStrategyParameters> simulationResult) {

        final List<CumulativeData> dataList = getData(simulationResult);
        final CumulativeData data =
                dataList
                        .stream()
                        .filter(d -> d.startHour() == decision.strategyParameters().getStartHour())
                        .filter(d -> d.startMinute() == decision.strategyParameters().getStartMinute())
                        .filter(d -> d.variance() == decision.strategyParameters().getVariance())
                        .findFirst()
                        .orElseThrow(() -> new UnsupportedOperationException("Impossible error"));

        final List<CumulativeStrategyReportEntry> entries = new ArrayList<>();

        pairs.forEach(pair -> entries.addAll(data.entries().subList(pair.getValue0(), pair.getValue1())));

        return entries;
    }


    //  INNER CLASSES/RECORDS

    /**
     * Class representation of a file of Cumulative data
     *
     * @param startHour   start hour
     * @param startMinute start minute
     * @param variance    variance
     * @param entries     {@link List} of {@link CumulativeStrategyReportEntry}
     */
    private record CumulativeData(int startHour, int startMinute, double variance,
                                  List<CumulativeStrategyReportEntry> entries) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CumulativeData that = (CumulativeData) o;
            return startHour == that.startHour && startMinute == that.startMinute && Double.compare(variance, that.variance) == 0;
        }

        @Override
        public int hashCode() {
            int result = startHour;
            result = 31 * result + startMinute;
            result = 31 * result + Double.hashCode(variance);
            return result;
        }
    }
}
