module compiler.plugin.test.plugin.with.two.dependencies {
    requires io.ballerina.lang;
    requires io.ballerina.tools.api;
    requires compiler.plugin.test.diagnostic.utils.lib;

    exports io.samjs.plugins.twodependencies;
}
