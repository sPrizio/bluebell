package com.bluebell.platform.models.core.entities.news;

import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a market news entry which represents a time of day that can have 1 or more pieces of news
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
@Entity
@Builder
@Table(name = "market_news_slots")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MarketNewsSlot implements GenericEntity, Comparable<MarketNewsSlot> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude LocalTime time;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private @EqualsAndHashCode.Exclude @ToString.Exclude MarketNews news;

    @Setter
    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("severity DESC")
    private @Builder.Default @EqualsAndHashCode.Exclude List<MarketNewsEntry> entries = new ArrayList<>();


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link MarketNewsEntry}
     */
    public void addEntry(MarketNewsEntry entry) {

        if (CollectionUtils.isEmpty(this.entries)) {
            this.entries = new ArrayList<>();
        }

        this.entries.add(entry);
        entry.setSlot(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link MarketNewsEntry}
     */
    public void removeEntry(MarketNewsEntry entry) {
        if (CollectionUtils.isNotEmpty(this.entries)) {
            final List<MarketNewsEntry> e = new ArrayList<>(this.entries);
            e.remove(entry);
            this.entries = e;
            entry.setSlot(null);
        }
    }

    @Override
    public int compareTo(MarketNewsSlot o) {
        return this.time.compareTo(o.time);
    }
}
