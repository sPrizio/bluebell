open module com.bluebell.radicle {
    exports com.bluebell.radicle.enums;
    exports com.bluebell.radicle.parsers.impl;
    requires org.apache.commons.text;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.commons.io;
    requires com.bluebell.platform;
    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;
}