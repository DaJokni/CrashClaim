package net.crashcraft.whipclaim.claimobjects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class,
        property = "@object_id")
@JsonTypeName("SubClaim")
public class SubClaim extends BaseClaim {
    private Claim parent;

    public SubClaim() {

    }

    public SubClaim(Claim parent, int id, int upperCornerX, int upperCornerY, int lowerCornerX, int lowerCornerY, UUID world, PermissionGroup perms) {
        super(id, upperCornerX, upperCornerY, lowerCornerX, lowerCornerY, world, perms);
        this.parent = parent;
    }

    @Override
    public void setToSave(boolean toSave) {
        if (parent == null){
            // Needed for json setting this should never happen after load
            return;
        }
        parent.setToSave(true);
    }

    @Override
    public boolean isToSave() {
        return parent.isToSave();
    }

    public Claim getParent() {
        return parent;
    }

    //JSON needs this

    public void setParent(Claim parent) {
        this.parent = parent;
    }

}
