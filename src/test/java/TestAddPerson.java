import org.example.Person;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestAddPerson {

    @Test
    public void correctDetails() {
        Person p = new Person();

        assertEquals(true,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22-02-2005"));
    }

    @Test
    public void personIdLong() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("23232234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22-02-2005"));
    }

    @Test
    public void personIdShort() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "04-12-2005"));
    }

    @Test
    public void personIdWrongFirstTwoCharacters() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("0034567890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "04-12-2005"));
    }

    @Test
    public void personIdNoSpecialCharacters() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234567890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "04-12-2005"));
    }

    @Test
    public void addressWrongFormat() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32 Highland Street Melbourne Victoria Australia",
                        "04-12-2005"));
    }

    @Test
    public void addressNumberInvalid() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "AB|Highland Street|Melbourne|Victoria|Australia",
                        "04-12-2005"));
    }

    @Test
    public void addressStateNotVictoria() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|New South Wales|Australia",
                        "04-12-2005"));
    }

    @Test
    public void addressCountryNotAustralia() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|New Zealand",
                        "04-12-2005"));
    }

    @Test
    public void birthdateNotADate() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "hello"));
    }

    @Test
    public void birthdateWrongFormat() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22/02/2005"));
    }

    @Test
    public void birthdateInvalidDate() {
        Person p = new Person();

        assertEquals(false,
                p.addPerson("2234??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22-56-2005"));
    }

}
