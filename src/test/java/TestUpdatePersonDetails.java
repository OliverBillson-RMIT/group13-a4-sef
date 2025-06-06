import org.example.Person;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestUpdatePersonDetails {

    @Test
    public void correctDetails() {
        Person p = new Person();

        // Orignal person data.
        p.addPerson("2234??7890", "Julian", "Bashir",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "22-02-2005");

        assertEquals(true,
                p.updatePersonalDetails("3334??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22-02-2005"));
    }

    @Test
    public void under18ChangeAddress() {
        Person p = new Person();

        // Orignal person data.
        p.addPerson("2234??7890", "Julian", "Bashir",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "22-02-2005");

        assertEquals(false,
                p.updatePersonalDetails("3334??7890", "Julian", "Bashir",
                        "19|Some Road|Melbourne|Victoria|Australia",
                        "22-02-2010"));
    }

    @Test
    public void birthdateAndFirstnameChange() {
        Person p = new Person();

        // Orignal person data.
        p.addPerson("2234??7890", "Julian", "Bashir",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "22-02-2005");

        assertEquals(false,
                p.updatePersonalDetails("3334??7890", "John", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "23-12-2005"));
    }

    @Test
    public void personIDFirstDigitEven() {
        Person p = new Person();

        // Orignal person data.
        p.addPerson("2234??7890", "Julian", "Bashir",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "22-02-2005");

        assertEquals(false,
                p.updatePersonalDetails("6634??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22-02-2005"));
    }

    @Test
    public void birthdateInvalidFormat() {
        Person p = new Person();

        // Orignal person data.
        p.addPerson("2234??7890", "Julian", "Bashir",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "22-02-2005");

        assertEquals(false,
                p.updatePersonalDetails("3334??7890", "Julian", "Bashir",
                        "32|Highland Street|Melbourne|Victoria|Australia",
                        "22/03/2005"));
    }
}
