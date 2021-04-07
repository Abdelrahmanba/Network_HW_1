package P2P.Search;

import P2P.Status;

import java.util.ArrayList;

public class SearchByUsername implements SearchUser {

    private String username;
    private ArrayList<Status> listOfUsers;

    public SearchByUsername(ArrayList<Status> listOfUsers, String username) {
        this.listOfUsers = listOfUsers;
        this.username = username;
    }

    @Override
    public Status search() {
        Status targetUser = null;
        for (Status status : listOfUsers)
            if (status.getUsername().equals(username)) {
                targetUser = status;
                break;
            }

        return targetUser;
    }

}
