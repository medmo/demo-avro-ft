package fr.francetravail.eda.demo;

import fr.ft.evenement.UserAction;
import fr.ft.evenement.UserProfile;
import fr.ft.evenement.UserUpdated;

public class Demo {

    public static void main(String[] args) {

        UserUpdated.Builder builder = UserUpdated.newBuilder();
        UserProfile.Builder profile = UserProfile.newBuilder();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@test.sn");
        profile.setAge(30);

        builder.setUserId("1234");
        builder.setAction(UserAction.CREATED);
        builder.setProfile(profile.build());

        UserUpdated userUpdated = builder.build();

        System.out.println(userUpdated.toString());

    }
}
