package com.bluebell.platform.models.core.entities.news;

import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of market news on a specific day
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Builder
@Table(name = "market_news")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MarketNews implements GenericEntity, Comparable<MarketNews> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true)
    private LocalDate date;

    @Setter
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("time ASC")
    private @Builder.Default List<MarketNewsSlot> slots = new ArrayList<>();


    //  METHODS

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void addSlot(MarketNewsSlot slot) {

        if (CollectionUtils.isEmpty(this.slots)) {
            this.slots = new ArrayList<>();
        }

        this.slots.add(slot);
        slot.setNews(this);
    }

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void removeSlot(MarketNewsSlot slot) {
        if (CollectionUtils.isNotEmpty(this.slots)) {
            final List<MarketNewsSlot> entries = new ArrayList<>(this.slots);
            entries.remove(slot);
            this.slots = entries;
            slot.setNews(null);
        }
    }

    @Override
    public int compareTo(MarketNews o) {
        return this.date.compareTo(o.date);
    }
}
