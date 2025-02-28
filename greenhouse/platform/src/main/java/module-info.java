module com.bluebell.platform {
    exports com.bluebell.platform.services;
    exports com.bluebell.platform.util;

    exports com.bluebell.platform.enums.account;
    exports com.bluebell.platform.enums.trade;
    exports com.bluebell.platform.enums.time;
    exports com.bluebell.platform.enums.system;

    exports com.bluebell.platform.models.core.entities;
    opens com.bluebell.platform.models.core.entities;

    opens com.bluebell.platform.models.core.entities.news;
    opens com.bluebell.platform.models.core.entities.account;
    opens com.bluebell.platform.models.core.entities.security;
    opens com.bluebell.platform.models.core.entities.system;
    opens com.bluebell.platform.models.core.entities.trade;
    opens com.bluebell.platform.models.core.entities.transaction;

    exports com.bluebell.platform.models.core.entities.account;
    exports com.bluebell.platform.models.core.entities.news;
    exports com.bluebell.platform.models.core.entities.security;
    exports com.bluebell.platform.models.core.entities.system;
    exports com.bluebell.platform.models.core.entities.trade;
    exports com.bluebell.platform.models.core.entities.transaction;

    exports com.bluebell.platform.models.core.nonentities.apexcharts;
    exports com.bluebell.platform.models.core.nonentities.data;
    exports com.bluebell.platform.models.core.nonentities.market;

    exports com.bluebell.platform.models.core.nonentities.records.account;
    exports com.bluebell.platform.models.core.nonentities.records.analysis;
    exports com.bluebell.platform.models.core.nonentities.records.portfolio;
    exports com.bluebell.platform.models.core.nonentities.records.trade;
    exports com.bluebell.platform.models.core.nonentities.records.tradelog;
    exports com.bluebell.platform.models.core.nonentities.records.traderecord;

    exports com.bluebell.platform.models.api.dto.account;
    exports com.bluebell.platform.models.api.dto.news;
    exports com.bluebell.platform.models.api.dto.security;
    exports com.bluebell.platform.models.api.dto.system;
    exports com.bluebell.platform.models.api.dto.trade;
    exports com.bluebell.platform.models.api.dto.transaction;

    opens com.bluebell.platform.models.api.json;
    exports com.bluebell.platform.models.api.json;
    exports com.bluebell.platform.models.core.nonentities.records.traderecord.controls;
    exports com.bluebell.platform.models.api.dto;
    exports com.bluebell.platform.enums.security;
    exports com.bluebell.platform.constants;
    exports com.bluebell.platform.enums.analysis;
    exports com.bluebell.platform.enums.chart;
    exports com.bluebell.platform.exceptions.system;
    exports com.bluebell.platform.enums.news;
    exports com.bluebell.platform.enums.transaction;
    exports com.bluebell.platform.enums.strategy;
    exports com.bluebell.platform.exceptions.calculator;

    requires lombok;
    requires org.apache.commons.collections4;
    requires org.apache.commons.lang3;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.persistence;
    requires com.fasterxml.jackson.annotation;
    requires org.slf4j;
}