# Step 0. Run by Maven
### Mac
```bash
./mvnw compile exec:java -Dexec.mainClass="org.example.Main"
```
### Windows PowerShell
```ps
.\mvnw compile exec:java -Dexec.mainClass="org.example.Main"
```

# Step 1. Add JUnit Dependency To Project pom.xml
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

# Step 2. Create Unit Test Class Under /test/java
![Create Test Class In Intellij](/screen_cap_for_readme/create_test_class_in_intellij.png)
## Create For Main Class
![Create Test Class For Main](/screen_cap_for_readme/create_test_class_for_Main.png)
## Create For ProductionDiscountIdentifier Class
![Create Test Class For ProductionDiscountIdentifier](/screen_cap_for_readme/create_test_class_for_ProductionDiscountIdentifier.png)
## Expected Folder Structure
![Expected Folder Structure](/screen_cap_for_readme/test_class_folder_location.png)

# Step 3. Add JUnit Maven Plugin to pom.xml
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

# Step 4. Run Test
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

# Step 5. Add JaCoCo Plugin
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

# Step 6. Run Test With Coverage Report
### Mac
```bash
./mvnw clean verify
```
### Windows PowerShell
```ps
.\mvnw clean verify
```

### Open index.html
![Open index.html](/screen_cap_for_readme/open_jacoco_report.png)

### Report Example
![JaCoCo Report Example](/screen_cap_for_readme/jacoco_report.png)

# Step 7. Full Coverage Unit Test Code
can copy and paste to the two unit test class file
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

# Step 8. Run Test With Coverage Report Again
### Mac
```bash
./mvnw clean verify
```
### Windows PowerShell
```ps
.\mvnw clean verify
```

### Open index.html
![Open index.html](/screen_cap_for_readme/open_jacoco_report.png)

### Check Report with Full Coverage
![Check Report with Full Coverage](/screen_cap_for_readme/jacoco_report_full_coverage.png)