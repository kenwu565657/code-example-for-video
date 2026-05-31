# Run by Maven
```shell
./mvnw compile exec:java -Dexec.mainClass="org.example.Main"
```

# Add JUnit Dependency To Project
```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

# Add JUnit Maven Plugin
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.1.2</version>
        </plugin>
    </plugins>
</build>
```

# Run Test
### Mac
```bash
./mvnw clean test
```
### Windows PowerShell
```ps
.\mvnw clean test
```

### Expected Result
```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

# Add JaCoCo Plugin
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.14</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.95</minimum>
                            </limit>
                            <limit>
                                <counter>BRANCH</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.95</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Report Example
![JaCoCo Report Example](/screen_cap_for_readme/jacoco_report.png)

# Full Coverage Unit Test Code
```java
package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final InputStream standardIn = System.in;
    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(standardIn);
        System.setOut(standardOut);
    }

    private void simulateUserInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testMain_ValidInputWithDiscount() {
        simulateUserInput("100.0\ny\n");

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Enter your product price: "));
        assertTrue(output.contains("Enter your product have discount or not"));
        assertTrue(output.contains("Your product have a discount of"));
    }

    @Test
    void testMain_ValidInputWithoutDiscount() {
        simulateUserInput("50.0\nn\n");

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Your product have a discount of"));
    }

    @Test
    void testMain_InvalidNumberFormat_ThrowsException() {
        simulateUserInput("abc\n");

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Invalid number input!"));
    }

    @Test
    void testMain() {
        Main main = new Main();
        Assertions.assertInstanceOf(Main.class, main);
    }
}
```

```java
package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductionDiscountIdentifierTest {

    private final ProductionDiscountIdentifier identifier = new ProductionDiscountIdentifier();

    @Test
    void identify_WhenProductIsNull_ThrowsEmptyProductException() {
        Assertions.assertThrows(EmptyProductException.class, () -> identifier.identify(null));
    }

    @Test
    void identify_WhenProductHasNoDiscount_ReturnsZero() throws EmptyProductException {
        Product product = new Product(500.0, false);

        float result = identifier.identify(product);
        Assertions.assertEquals(0f, result);
    }

    @Test
    void identify_WhenPriceIsUnder100_Returns10() throws EmptyProductException {
        Product product = new Product(99.0, true);

        float result = identifier.identify(product);
        Assertions.assertEquals(10f, result);
    }

    @Test
    void identify_WhenPriceIsExactly100_Returns20() throws EmptyProductException {
        Product product = new Product(100.0, true);

        float result = identifier.identify(product);
        Assertions.assertEquals(20f, result);
    }

    @Test
    void identify_WhenPriceIs250_Returns40() throws EmptyProductException {
        Product product = new Product(250.0, true);

        float result = identifier.identify(product);
        Assertions.assertEquals(40f, result);
    }

    @Test
    void identify_WhenPriceIs300OrMore_Returns50() throws EmptyProductException {
        Product product = new Product(300.0, true);

        float result = identifier.identify(product);
        Assertions.assertEquals(50f, result);
    }
}
```
