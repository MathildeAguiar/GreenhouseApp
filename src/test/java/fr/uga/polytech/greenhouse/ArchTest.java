package fr.uga.polytech.greenhouse;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.uga.polytech.greenhouse");

        noClasses()
            .that()
            .resideInAnyPackage("fr.uga.polytech.greenhouse.service..")
            .or()
            .resideInAnyPackage("fr.uga.polytech.greenhouse.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..fr.uga.polytech.greenhouse.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
