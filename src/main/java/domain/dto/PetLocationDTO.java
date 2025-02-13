package domain.dto;

import domain.model.PetLocation;

public class PetLocationDTO {
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String address;

    public PetLocationDTO(PetLocation petLocation) {
        this.country = petLocation.getCountry();
        this.state = petLocation.getState();
        this.city = petLocation.getCity();
        this.neighborhood = petLocation.getNeighborhood();
        this.address = petLocation.getAddress();
    }

    public PetLocationDTO() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }


    public String getNeighborhood() {
        return neighborhood;
    }

    public String getAddress() {
        return address;
    }

}