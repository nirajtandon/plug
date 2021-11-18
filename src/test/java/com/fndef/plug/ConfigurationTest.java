package com.fndef.plug;

public class ConfigurationTest {
    public static void main(String[] args) throws Exception {
        new ConfigurationTest().run();
    }

    private void run() {
        Context c = new ContextBuilder.WithOptions(ConfigFormat.XML)
                .configSource(getClass().getClassLoader().getResourceAsStream("simple-config.xml"))
                .getContext();

        // Test t = c.getById("id1", Test.class);
        // System.out.println(t.get());
    }
}
