open module com.bluebell.radicle {
    exports com.bluebell.radicle.enums;
    exports com.bluebell.radicle.models;
    exports com.bluebell.radicle.parsers.impl;
    requires com.bluebell;
    requires org.apache.commons.collections4;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.commons.io;
    requires static lombok;
}