# ModTemplate

A [GitHub template repository](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template)
for Minecraft **1.12.2 Forge** mods, built around
[RetroFuturaGradle](https://github.com/GTNewHorizons/RetroFuturaGradle), [JvmDowngrader](https://github.com/uniminers/jvmdowngrader), and [Lwjgl3ify](https://github.com/TwilightFlower/lwjgl3ify).

Heavily inspired by [GregTechCEu/Buildscripts](https://github.com/GregTechCEu/Buildscripts)
but uses a modern, modular **build-logic** setup instead of a single `build.gradle`.

> **Note:** Click **"Use this template"** on GitHub to create your own mod repository, then rename
> the placeholder package/classes, fill in `gradle.properties`, and start writing your mod.

---

## Table of Contents

- [Features](#features)
- [Quick Start](#quick-start)
- [Project Layout](#project-layout)
- [Configuring Your Mod](#configuring-your-mod)
- [Adding Dependencies](#adding-dependencies)
- [Common Gradle Tasks](#common-gradle-tasks)
- [Modern Java Runtime (LWJGL3ify)](#modern-java-runtime-lwjgl3ify)
- [Mixins](#mixins)
- [Interface Injection](#interface-injection)
- [Access Transformers](#access-transformers)
- [CoreMods](#coremods)
- [Publishing](#publishing)
- [Differences from GTCEu Buildscripts](#differences-from-gtceu-buildscripts)
- [License](#license)

---

## Features

- **Modern Java → Java 8** — compile with Java 25 using
  [JvmDowngrader](https://github.com/uniminers/jvmdowngrader), then downgrade to Java 8 bytecode.
  Unlike Jabel, which only allows syntax up to Java 17, JvmDowngrader also handles many Java 9–25
  library APIs via shims.
- **Modern Java run tasks** — `runClientModernJava` and `runServerModernJava` run on Java 25 with
  [Lwjgl3ify](https://github.com/TwilightFlower/lwjgl3ify) + [ForgePatches Extra](https://github.com/MCTian-mi/ForgePatches-Extra).
- **Modular build logic** — Kotlin DSL convention plugins in `build-logic/convention`.
- **Version catalogs** for plugins and dependencies.
- **Interface injection** via RetroFuturaGradle's `applyJST` task.
- **Java 8 test runtime** — tests are compiled on Java 25, downgraded to Java 8 bytecode, and
  executed on a Java 8 JVM to prove the downgrade is correct end-to-end.
- **Mixins, Access Transformers, Coremods, Shadow, JUnit, and GitHub Actions
  Publishing** out of the box.

---

## Quick Start

1. **Create your repo from this template.**

   On GitHub, click **"Use this template"** and create a new repository. Then clone it and open it
   in IntelliJ IDEA (recommended) or your IDE of choice.

   > Do not fork this repository — a fork carries the upstream git history and is meant for
   > contributing back, not for starting a new mod.
   >
2. **Set your mod identity in `gradle.properties`.**

   At minimum edit these fields:

   ```properties
   modName = My Awesome Mod
   modId = myawesomemod
   modGroup = com.yourname.myawesomemod
   modVersion = 1.0.0
   archiveName = myawesomemod
   ```
3. **Rename the placeholder source package.**

   The template uses `com.myname.mymodid`. Rename the directory and update the class/package names
   to match `modGroup`.
4. **Update `mcmod.info` (optional).**

   The build will substitute `@mod_id@`, `@mod_name@`, `@mod_version@`, etc. automatically, but you
   can still fill in description, author list, url, etc.
5. **Run the modded client.**

   ```bash
   ./gradlew runClient
   ```

   Or, to test on a modern Java runtime with LWJGL3:

   ```bash
   ./gradlew runClientModernJava
   ```

---

## Project Layout

```text
.
├── build-logic/
│   └── convention/
│       └── src/main/kotlin/
│           ├── minecraft.gradle.kts    # RFG, mcmod.info templating, run tasks
│           ├── publish.gradle.kts      # CurseForge / Modrinth / Maven / GitHub
│           ├── shadow.gradle.kts       # Shadow + dependency relocation
│           ├── jvmdg.gradle.kts        # JvmDowngrader integration
│           ├── test.gradle.kts         # JUnit 5 setup (runs on Java 8)
│           ├── idea.gradle.kts         # IntelliJ IDEA project settings
│           ├── jvm.gradle.kts          # Java toolchain (Java 25)
│           ├── repositories.gradle.kts # Maven repositories
│           └── Properties.kt           # Type-safe accessors for gradle.properties
├── gradle/
│   ├── libs.versions.toml              # Build plugin & runtime library versions
│   └── deps.versions.toml              # Mod-specific dependency versions
├── src/
│   ├── main/
│   │   ├── java/                       # Your mod source code
│   │   └── resources/                  # mcmod.info, pack.mcmeta, ATs, mixin configs
│   ├── injectedInterfaces/             # RFG interface injection configs
│   └── test/                           # JUnit tests
├── .github/
│   ├── actions/build_setup/            # Reusable JDK/Gradle setup action
│   └── workflows/
│       ├── build.yml                   # Build artifacts on push/PR
│       └── publish.yml                 # Publish releases
├── build.gradle.kts                    # Applies convention plugins & declares deps
├── settings.gradle.kts                 # Project name, version catalogs, build-logic
└── gradle.properties                   # User-facing mod configuration
```

---

## Configuring Your Mod

Most settings live in `gradle.properties`. Below are the commonly edited options.


| Property             | Description                                                                                    |
| -------------------- | ---------------------------------------------------------------------------------------------- |
| `modName`            | Human-readable mod name.                                                                       |
| `modId`              | Lowercase, no spaces, unique mod identifier (< 64 chars).                                      |
| `modGroup`           | Root Java package for your mod, e.g. `com.yourname.modid`.                                     |
| `modVersion`         | Mod version. Leave blank to use the latest git tag (`git describe`).                           |
| `archiveName`        | Base name of produced jars.                                                                    |
| `minecraftVersion`   | Minecraft version. Currently locked to `1.12.2`.                                               |
| `devUserName`        | In-game username used by `runClient`. Defaults to `Developer`.                                 |
| `useLwjgl3ify`       | Enable modern-Java run tasks via lwjgl3ify. Default `true`.                                     |
| `generateTags`       | Generate a `Tags` class with `MOD_ID`, `MOD_NAME`, `MOD_VERSION`, `MC_VERSION`. Default `true`.|
| `accessTransformers` | Semicolon-separated list of AT cfg files in `src/main/resources/`.                             |
| `useMixin`           | Enable MixinBooter support. Default `false`.                                                   |
| `mixinPackage`       | Package containing all your mixin classes (required if `useMixin=true`).                       |
| `coreModClass`       | Fully-qualified `IFMLLoadingPlugin` class if you use a core mod.                                |
| `enableJUnit`        | Enable JUnit 5 test platform. Default `true`.                                                  |
| `enableSpotless`     | Enable Spotless code formatting. Default `false`.                                              |

Publishing options are documented in the [Publishing](#publishing) section.

---

## Adding Dependencies

Dependencies are declared in Gradle version catalogs and then referenced in `build.gradle.kts`.

1. Add the dependency to the appropriate version catalog.

   For mod dependencies, edit `gradle/deps.versions.toml`:

   ```toml
   [versions]
   fluidloggedApi = "3.1.1"

   [libraries]
   fluidloggedApi = { module = "maven.modrinth:fluidlogged-api", version.ref = "fluidloggedApi" }
   ```

   For build/runtime infrastructure libraries, edit `gradle/libs.versions.toml`.
2. **Reload/Resync the Gradle project** after editing the catalog so Gradle regenerates the
   type-safe accessors (`deps.fluidloggedApi`) used in build scripts.
3. Declare it in `build.gradle.kts`:

   ```kotlin
   dependencies {
       implementation(deps.fluidloggedApi)
       compileOnly(deps.someApi)
       runtimeOnly(deps.someRuntimeMod)

       // Include in the obfuscated release jar, downgraded and relocated
       shadowDowngrade(deps.someModernLibrary)
   }
   ```

Available dependency configurations:


| Configuration                    | Purpose                                               |
| -------------------------------- | ----------------------------------------------------- |
| `implementation` / `api`         | Compile and runtime classpath.                        |
| `compileOnly` / `compileOnlyApi` | Compile-only, not packaged.                           |
| `runtimeOnly`                    | Runtime-only, e.g. JEI/TOP in dev.                    |
| `shadowImplementation`           | Bundled into the shadow jar.                          |
| `shadowDowngrade`                | Bundled and downgraded to Java 8 for the release jar. |

Maven repositories are configured in `build-logic/convention/src/main/kotlin/repositories.gradle.kts`
and include Modrinth, CurseMaven, CleanroomMC, BlameJared, GTNH, GTCEu, Maven Central, and Maven Local.

---

## Common Gradle Tasks


| Task                            | Description                                                        |
| ------------------------------- | ------------------------------------------------------------------ |
| `./gradlew build`               | Build the mod jar, sources jar, shadowed jar, and downgraded jars. |
| `./gradlew runClient`           | Launch the modded client on Java 8.                                |
| `./gradlew runServer`           | Launch the modded dedicated server on Java 8.                      |
| `./gradlew runClientModernJava` | Launch the client on modern Java (25) via lwjgl3ify.               |
| `./gradlew runServerModernJava` | Launch the server on modern Java (25) via lwjgl3ify.               |
| `./gradlew test`                | Run JUnit tests on a real Java 8 JVM (tests are compiled on Java 25 and downgraded first).       |
| `./gradlew reobfJar`            | Produce the obfuscated release jar.                                |
| `./gradlew curseforge`          | Publish to CurseForge (requires env vars).                         |
| `./gradlew modrinth`            | Publish to Modrinth (requires env vars).                           |
| `./gradlew publish`             | Publish to a custom Maven repository (requires env vars).          |
| `./gradlew build --build-cache` | Build with Gradle build cache enabled.                             |

---

## Testing

Tests are written with JUnit 5 and live under `src/test/java`. Because the mod is compiled on Java 25
but ships as Java 8 bytecode, the build also runs tests on a **real Java 8 JVM**:

1. `compileTestJava` compiles test sources with the Java 25 toolchain.
2. `downgradeTestClasses` rewrites the compiled test bytecode to Java 8.
3. `downgradeMainClasses` rewrites the project's main bytecode to Java 8 so tests can load it.
4. `test` launches the JUnit Platform on an automatically-provisioned **Azul Zulu Java 8** toolchain
   using the downgraded classes.

This proves the JvmDowngrader output is actually runnable on Java 8, not just syntactically valid.

```bash
./gradlew test
```

Tests can reference Minecraft/Forge classes from `patchedMc` and `mcLauncher`, so you can write
smoke tests that exercise mod code against the real 1.12.2 runtime.

---

## Modern Java Runtime (LWJGL3ify)

By default `useLwjgl3ify = true`, which registers two extra tasks:

- `runClientModernJava`
- `runServerModernJava`

These tasks run the 1.12.2 client/server on a **JetBrains Runtime 25** toolchain using
[lwjgl3ify](https://github.com/twilightflower/lwjgl3ify) together with
[ForgePatches Extra](https://github.com/MCTian-mi/ForgePatches-Extra). The extra patches are
required because lwjgl3ify alone does not cover every JDK-internal reflection call that Forge and
1.12.2 mods rely on; ForgePatches Extra provides the additional compatibility shims so the game
starts and runs correctly on Java 25.

The required `--add-opens`, system classloader, and native-access flags are configured automatically.
Hotswap support can be toggled with `enableHotswap` / the `HOTSWAP` environment variable.

---

## Mixins

1. Set `useMixin=true` in `gradle.properties`.
2. Set `mixinPackage` to the package that will contain all your mixin classes.
3. Create mixin classes under that package.
4. Edit `src/main/resources/mymod.default.mixins.json` (renamed to `mixins.<modId>.json` at build
   time) and add your mixin classes.

The build automatically:

- Excludes `*mixin*.json` from resources when mixins are disabled.
- Renames and filters the mixin config with the correct `mod_id`, `mod_group`, `mixin_package`, etc.
- Sets up the MixinBooter annotation processor and refmap.
- Attaches MixinBooter as a `-javaagent` when both mixins and hotswap are enabled on modern Java
  runs.

---

## Interface Injection

RetroFuturaGradle can inject interfaces into existing Minecraft/Forge classes at decompile time via
its `applyJST` task. This is useful when you want to add helper methods or marker interfaces to
vanilla classes without mixins or reflection.

The build automatically picks up `src/injectedInterfaces/interfaces.json` if it exists. Interface
injection only works for **Minecraft and Forge classes**; it cannot inject into mod dependencies or
libraries.

### Example

Create `src/injectedInterfaces/interfaces.json`:

```json
{
  "net/minecraft/world/World": ["com/myname/mymodid/Foo"]
}
```

And the matching interface:

```java
package com.myname.mymodid;

public interface Foo {
    default void bar() {
        throw new AssertionError();
    }
}
```

After the next decompile/rebuild, `World` will implement `Foo` and instances of `World` can call
`bar()` directly.

> **Note:** Interface injection happens at decompile time, so the injected interface and its methods
> are visible in IDE source navigation and to other transformers. Do not use it for behaviour that
> must change at runtime based on state.

---

## Access Transformers

Minecraft and Forge classes are transformed with the legacy Forge AccessTransformer, as usual. In
addition, this buildscript includes
[ForgeGradle 7's standalone AccessTransformers](https://github.com/MinecraftForge/AccessTransformers)
as an optional tool: you can apply ATs to **other dependencies** (for example, library mods your
project bundles or depends on), which GTCEu Buildscripts does not support.

### Example

1. Create an AT cfg file in `src/main/resources/`, e.g. `mymod_at.cfg`:

   ```text
   public net.minecraft.entity.item.EntityItem field_70292_b # age
   public net.minecraftforge.client.GuiIngameForge WHITE
   public net.minecraft.client.Minecraft field_71428_T # timer

   # AT applied to a dependency (requires FG7 standalone AT)
   public com.some.librarymod.SomeClass somePrivateField
   ```
2. Set it in `gradle.properties`:

   ```properties
   accessTransformers = mymod_at.cfg
   ```
3. Multiple files can be separated with `;`:

   ```properties
   accessTransformers = mymod_at.cfg;compat_at.cfg
   ```

The build will fail early if a configured AT file is missing. Lines targeting Minecraft/Forge use
the legacy Forge AT; lines targeting other dependencies use the FG7 standalone AccessTransformer.

---

## CoreMods

1. Set `coreModClass=<subpackage>.FMLPlugin` in `gradle.properties` (relative to `modGroup`).
2. Implement `IFMLLoadingPlugin` in that class.
3. The build will set `FMLCorePlugin` and related manifest attributes automatically.

If your project is *only* a core mod or mixin collection and has no `@Mod` class, set
`forceLoadAsMod=true`.

For debugging ASM transformations, set `enableCoreModDebug=true` to dump pre/post-transformed
classes to `run/CLASSLOADER_TEMP`.

---

## Publishing

Publishing is handled by the `publish.yml` GitHub Actions workflow and can also be run locally.

### Triggers

- **Tag push** matching `v*.*.*` — automatically publishes a GitHub Release, CurseForge, and
  Modrinth version.
- **Workflow dispatch** — choose version, release type, and which platforms to publish.

### Required Secrets / Environment Variables


| Variable                        | Used For                                          |
| ------------------------------- | ------------------------------------------------- |
| `CURSEFORGE_API_KEY`            | CurseForge upload.                                |
| `CURSEFORGE_PROJECT_ID`         | CurseForge project.                               |
| `MODRINTH_API_KEY`              | Modrinth upload.                                  |
| `MODRINTH_PROJECT_ID`           | Modrinth project.                                 |
| `MAVEN_USER` / `MAVEN_PASSWORD` | Custom Maven publishing.                          |
| `MOD_VERSION`                   | Override version (also set by CI from tag/input). |
| `RELEASE_TYPE`                  | `release`, `beta`, or `alpha`.                    |
| `DEPLOYMENT_DEBUG`              | Dry-run publishers without uploading.             |

### Relations

CurseForge and Modrinth relations are configured in `gradle.properties`:

```properties
curseForgeRelations = requiredDependency:some-mod;optionalDependency:another-mod
modrinthRelations = req:some-mod;opt:another-mod
```

---

## Differences from GTCEu Buildscripts

This template started from the same goals as
[GregTechCEu/Buildscripts](https://github.com/GregTechCEu/Buildscripts), but it diverges in a few
important ways.


| Aspect                       | GTCEu Buildscripts                                                                 | This template (MyMod)                                                                                            |
| ---------------------------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| **Build script structure**   | One self-contained, auto-updating `build.gradle` with an `updateBuildScript` task.  | Modular `build-logic` convention plugins written in Kotlin DSL.                                                   |
| **Updating**                 | Run `./gradlew updateBuildScript` to pull the latest build script.                  | Update by merging upstream `build-logic` changes manually or via git.                                             |
| **Modern language features** | Uses [Jabel](https://github.com/bsideup/jabel) to compile Java 17 syntax to Java 8. | Uses [JvmDowngrader](https://github.com/uniminers/jvmdowngrader) to downgrade a Java 25 build to Java 8 bytecode. |
| **Compile toolchain**        | Java 17.                                                                           | Java 25 (Azul Zulu via Foojay).                                                                                  |
| **Dependency management**    | Hand-edited dependency blocks.                                                     | Gradle version catalogs (`libs.versions.toml` / `deps.versions.toml`).                                           |
| **Mixin stack**              | MixinBooter 8 + Unimix.                                                            | MixinBooter 11.1.                                                                                                |
| **JUnit**                    | JUnit 5.                                                                           | JUnit 5 (tests run on Java 8 via downgraded bytecode).                                                           |
| **LWJGL3ify**                | Optional / manually configured.                                                    | Enabled by default (`useLwjgl3ify=true`) with dedicated modern-Java run tasks.                                   |
| **CI/CD**                    | Minimal / user-provided.                                                           | GitHub Actions workflows for build and publish, including GitHub Releases, CurseForge, Modrinth, and Maven.      |
| **Version catalogs**         | Not used.                                                                          | Used for plugins and libraries.                                                                                  |
| **Code formatting**          | Spotless enabled by default.                                                       | Spotless disabled by default (`enableSpotless=false`).                                                           |

Both setups target the same end result — a modern, productive 1.12.2 mod development workflow — but
this template trades the “one-click build-script update” convenience of GTCEu Buildscripts for more
explicit, version-controlled, and Gradle-idiomatic build logic.

---

## License

This template is released under the [MIT License](LICENSE).
