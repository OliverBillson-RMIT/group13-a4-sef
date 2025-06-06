import org.example.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAddDemeritPoints {

    @Test
    void testValidAdd_NotSuspended() {
        Person person = new Person();
        person.addPerson("23s_d%&fAB", "Julian", "Bashir",
            "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2006");
        String result = person.addDemeritPoints("25-05-2024", 4);
        assertEquals("Success", result);
        assertFalse(person.isSuspended());
    }

    @Test
    void testInvalidDateFormat() {
        Person person = new Person();
        person.addPerson("23s_d%&fAB", "Julian", "Bashir",
            "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2006");
        String result = person.addDemeritPoints("2024/05/25", 3);
        assertEquals("Failed", result);
    }

    @Test
    void testPointsOutOfRange() {
        Person person = new Person();
        person.addPerson("23s_d%&fAB", "Julian", "Bashir",
            "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2006");
        String result1 = person.addDemeritPoints("25-05-2024", 0);
        String result2 = person.addDemeritPoints("25-05-2024", 7);
        assertEquals("Failed", result1);
        assertEquals("Failed", result2);
    }

    @Test
    void testNoPersonAdded() {
        Person person = new Person();
        String result = person.addDemeritPoints("25-05-2024", 3);
        assertEquals("Failed", result);
    }

    @Test
    void testOldDemeritPointsNotCounted() {
        Person person = new Person();
        person.addPerson("23s_d%&fAB", "Julian", "Bashir",
            "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2006");
        person.addDemeritPoints("25-05-2021", 6);
        String result = person.addDemeritPoints("25-05-2024", 1);
        assertEquals("Success", result);
        assertFalse(person.isSuspended());
    }

    @Test
    void testNoSuspension21OrOlderWith12Points() {
        Person person = new Person();
        person.addPerson("23s_d%&fAB", "Julian", "Bashir",
            "32|Highland Street|Melbourne|Victoria|Australia", "01-01-1990");
        person.addDemeritPoints("25-05-2023", 6);
        String result = person.addDemeritPoints("25-05-2024", 6);
        assertEquals("Success", result);
        assertFalse(person.isSuspended());
    }
}
