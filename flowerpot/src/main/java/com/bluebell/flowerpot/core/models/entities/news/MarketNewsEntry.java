package com.bluebell.flowerpot.core.models.entities.news;

import com.bluebell.flowerpot.core.enums.news.MarketNewsSeverity;
import com.bluebell.flowerpot.core.enums.system.Country;
import com.bluebell.flowerpot.core.models.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representation of market news entry, a piece of news at a specific time
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Getter
@Entity
@Table(name = "market_news_entries")
public class MarketNewsEntry implements GenericEntity, Comparable<MarketNewsEntry> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String content;

    @Setter
    @Column
    private MarketNewsSeverity severity;

    @Setter
    @Column
    private Country country;

    @Setter
    @Column
    private String forecast;

    @Setter
    @Column
    private String previous;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MarketNewsSlot slot;


    //  METHODS

    @Override
    public int compareTo(MarketNewsEntry o) {
        return this.content.compareTo(o.content);
    }
}
