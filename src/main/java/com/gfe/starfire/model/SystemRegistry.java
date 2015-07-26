package com.gfe.starfire.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.gfe.starfire.model.system.ASystem;
import com.gfe.starfire.model.system.BlackHoleSystem;
import com.gfe.starfire.model.system.MultiStarSystem;
import com.gfe.starfire.model.system.NebulaSystem;
import com.gfe.starfire.model.system.NexusSystem;
import com.gfe.starfire.model.system.PlanetaryNebulaSystem;
import com.gfe.starfire.model.system.ProtostarSystem;
import com.gfe.starfire.model.system.StarSystem;
import com.gfe.starfire.model.system.feature.Star;
import com.gfe.starfire.model.system.feature.Star.Luminosity;
import com.gfe.starfire.model.system.feature.Star.SpectralClass;
import com.gfe.starfire.util.Chooser;
import com.gfe.starfire.util.Chooser.Option;

public class SystemRegistry {
    private final Random random;
    private final long seed;
    private final Map<Integer, ASystem> systems;

    private static Chooser<Generator<ASystem>> systemChooser;
    private static Chooser<Generator<Star>> starChooser;
    private static Chooser<SpectralClass> mainSequenceSpectralClassChooser;
    private static Chooser<SpectralClass> giantSpectralClassChooser;
    private static Chooser<SpectralClass> redDwarfSpectralClassChooser;
    private static Chooser<SpectralClass> whiteDwarfSpectralClassChooser;
    private static Chooser<Luminosity> giantLuminosityChooser;

    public static void main(final String[] args) {
        final SystemRegistry gen = new SystemRegistry(0);
        for (int i = 0; i < 1000; i++) {
            final ASystem system = gen.getSystem(i);
            System.out.println(system);
        }
    }

    public SystemRegistry() {
        this(new Random().nextLong());
    }

    public SystemRegistry(final long seed) {
        this.seed = seed;
        random = new Random();
        systems = new HashMap<>();

        systemChooser = new Chooser<Generator<ASystem>>(
                new Option<Generator<ASystem>>(SystemRegistry::generateStarSystem, 5000),
                new Option<Generator<ASystem>>(SystemRegistry::generateBinarySystem, 2000),
                new Option<Generator<ASystem>>(SystemRegistry::generateTrinarySystem, 500),
                new Option<Generator<ASystem>>(SystemRegistry::generateQuaternarySystem, 100),
                new Option<Generator<ASystem>>(SystemRegistry::generateNexusSystem, 700),
                new Option<Generator<ASystem>>(SystemRegistry::generateNebulaSystem, 800),
                new Option<Generator<ASystem>>(SystemRegistry::generateProtostarSystem, 100),
                new Option<Generator<ASystem>>(SystemRegistry::generatePlanetaryNebulaSystem, 400),
                new Option<Generator<ASystem>>(SystemRegistry::generateBlackHoleSystem, 5));

        starChooser = new Chooser<Generator<Star>>(
                new Option<Generator<Star>>(SystemRegistry::generateMainSequenceStar, 90),
                new Option<Generator<Star>>(SystemRegistry::generateGiantStar, 6),
                new Option<Generator<Star>>(SystemRegistry::generateRedDwarf, 10),
                new Option<Generator<Star>>(SystemRegistry::generateWhiteDwarf, 6));

        mainSequenceSpectralClassChooser = new Chooser<SpectralClass>(
                new Option<SpectralClass>(SpectralClass.O, 1),
                new Option<SpectralClass>(SpectralClass.B, 3),
                new Option<SpectralClass>(SpectralClass.A, 6),
                new Option<SpectralClass>(SpectralClass.F, 10),
                new Option<SpectralClass>(SpectralClass.G, 15),
                new Option<SpectralClass>(SpectralClass.K, 20),
                new Option<SpectralClass>(SpectralClass.M, 45));

        giantSpectralClassChooser = new Chooser<SpectralClass>(
                new Option<SpectralClass>(SpectralClass.O, 1),
                new Option<SpectralClass>(SpectralClass.B, 3),
                new Option<SpectralClass>(SpectralClass.A, 6),
                new Option<SpectralClass>(SpectralClass.K, 20),
                new Option<SpectralClass>(SpectralClass.M, 45));

        redDwarfSpectralClassChooser = new Chooser<SpectralClass>(
                new Option<SpectralClass>(SpectralClass.K, 20),
                new Option<SpectralClass>(SpectralClass.M, 45));

        whiteDwarfSpectralClassChooser = new Chooser<SpectralClass>(
                new Option<SpectralClass>(SpectralClass.B, 3),
                new Option<SpectralClass>(SpectralClass.A, 6),
                new Option<SpectralClass>(SpectralClass.F, 10));

        giantLuminosityChooser = new Chooser<Luminosity>(new Option<Luminosity>(Luminosity.Ia, 1),
                new Option<Luminosity>(Luminosity.Ib, 1), new Option<Luminosity>(Luminosity.II, 1),
                new Option<Luminosity>(Luminosity.III, 1),
                new Option<Luminosity>(Luminosity.IV, 1));
    }

    public ASystem getSystem(final int systemId) {
        ASystem system = systems.get(systemId);
        if (system == null) {
            random.setSeed((seed + systemId) * systemId);
            system = systemChooser.choose(random).generate(random);
            systems.put(systemId, system);
        }
        return system;
    }

    private interface Generator<T> {
        T generate(final Random random);
    }

    private static StarSystem generateStarSystem(final Random random) {
        return new StarSystem(starChooser.choose(random).generate(random));
    }

    private static MultiStarSystem generateBinarySystem(final Random random) {
        final Star star1 = starChooser.choose(random).generate(random);
        final Star star2 = starChooser.choose(random).generate(random);
        return new MultiStarSystem(star1, star2);
    }

    private static MultiStarSystem generateTrinarySystem(final Random random) {
        final Star star1 = starChooser.choose(random).generate(random);
        final Star star2 = starChooser.choose(random).generate(random);
        final Star star3 = starChooser.choose(random).generate(random);
        return new MultiStarSystem(star1, star2, star3);
    }

    private static MultiStarSystem generateQuaternarySystem(final Random random) {
        final Star star1 = starChooser.choose(random).generate(random);
        final Star star2 = starChooser.choose(random).generate(random);
        final Star star3 = starChooser.choose(random).generate(random);
        final Star star4 = starChooser.choose(random).generate(random);
        return new MultiStarSystem(star1, star2, star3, star4);
    }

    private static NexusSystem generateNexusSystem(final Random random) {
        return new NexusSystem();
    }

    private static NebulaSystem generateNebulaSystem(final Random random) {
        return new NebulaSystem();
    }

    private static ProtostarSystem generateProtostarSystem(final Random random) {
        return new ProtostarSystem();
    }

    private static PlanetaryNebulaSystem generatePlanetaryNebulaSystem(final Random random) {
        return new PlanetaryNebulaSystem();
    }

    private static BlackHoleSystem generateBlackHoleSystem(final Random random) {
        return new BlackHoleSystem();
    }

    private static Star generateMainSequenceStar(final Random random) {
        return new Star(mainSequenceSpectralClassChooser.choose(random), random.nextInt(10),
                Luminosity.V);
    }

    private static Star generateGiantStar(final Random random) {
        return new Star(giantSpectralClassChooser.choose(random), random.nextInt(10),
                giantLuminosityChooser.choose(random));
    }

    private static Star generateRedDwarf(final Random random) {
        return new Star(redDwarfSpectralClassChooser.choose(random), random.nextInt(10),
                Luminosity.VI);
    }

    public static Star generateWhiteDwarf(final Random random) {
        return new Star(whiteDwarfSpectralClassChooser.choose(random), random.nextInt(10),
                Luminosity.VII);
    }
}