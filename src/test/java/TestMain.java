import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMain {

    @BeforeAll
    public static void bootstrap() {
        Bootstrap.perform();
    }

    @Test
    public void modernSyntaxWorksOnJava8Runtime() {
        // Records (Java 16) compiled at Java 25, downgraded to Java 8
        record Point(int x, int y) {
        }

        var origin = new Point(0, 0);

        // instanceof record pattern (Java 21) — downgradable
        assertThat(origin).isInstanceOf(Point.class);
        if (origin instanceof Point(int x, int y)) {
            assertThat(x).isZero();
            assertThat(y).isZero();
        }
    }

    @Test
    public void minecraftCodeIsAccessible() {
        // Access Minecraft classes from the patched MC / launcher classpath
        Block stone = Objects.requireNonNull(Blocks.STONE);
        assertThat(stone.getRegistryName()).isEqualTo(new ResourceLocation("minecraft:stone"));
    }
}
