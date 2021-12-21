package si.fri.rso2021.Suggestions.models.v1.objects;

public class Customers {

    private Integer id;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private Integer postcode;
    private String town;
    private String birthDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Integer getPostcode() { return postcode; }

    public void setPostcode(Integer postcode) { this.postcode = postcode;}

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) { this.birthDate = birthDate;}


}

