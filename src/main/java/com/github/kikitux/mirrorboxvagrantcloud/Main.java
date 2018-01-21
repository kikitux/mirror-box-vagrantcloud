package com.github.kikitux.mirrorboxvagrantcloud;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.Arrays;
import java.util.List;

import static com.github.kikitux.mirrorboxvagrantcloud.VcmKt.halp;
import static com.github.kikitux.mirrorboxvagrantcloud.VcmKt.vcm;

public class Main {
    public static void main(String[] args) throws Exception {

        // if no arguments, we fail, exit 1 + help
        if (args.length < 1) {
            halp(1);
        }

        OptionParser parser = new OptionParser();

        parser.acceptsAll(Arrays.asList("h", "help"));
        parser.acceptsAll(Arrays.asList("v", "vagrant"));
        parser.acceptsAll(Arrays.asList("u", "user", "o", "org", "organization", "organisation"))
            .withRequiredArg();
        parser.acceptsAll(Arrays.asList("b", "box") )
            .withRequiredArg();
        parser.acceptsAll(Arrays.asList("p", "provider") )
                .withRequiredArg();
        parser.allowsUnrecognizedOptions();
        OptionSet options = parser.parse(args);
        vcm(options);
    }
}
