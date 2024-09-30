package com.bluebell.planter.core.models.entities.news;

import com.bluebell.planter.core.models.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of market news on a specific day
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Getter
@Entity
@Table(name = "market_news")
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
    private List<MarketNewsSlot> slots;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void addSlot(MarketNewsSlot slot) {

        if (getSlots() == null) {
            this.slots = new ArrayList<>();
        }

        getSlots().add(slot);
        slot.setNews(this);
    }

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void removeSlot(MarketNewsSlot slot) {
        if (getSlots() != null) {
            List<MarketNewsSlot> entries = new ArrayList<>(getSlots());
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
