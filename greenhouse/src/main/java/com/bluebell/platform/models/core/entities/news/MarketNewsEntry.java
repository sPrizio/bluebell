package com.bluebell.platform.models.core.entities.news;

import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class representation of market news entry, a piece of news at a specific time
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
@Entity
@Builder
@Table(name = "market_news_entries")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MarketNewsEntry implements GenericEntity, Comparable<MarketNewsEntry> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude String content;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude MarketNewsSeverity severity;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude Country country;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude String forecast;

    @Setter
    @Column
    private @EqualsAndHashCode.Exclude String previous;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private @EqualsAndHashCode.Exclude @ToString.Exclude MarketNewsSlot slot;


    //  METHODS

    @Override
    public int compareTo(MarketNewsEntry o) {
        return this.content.compareTo(o.content);
    }
}
