package me.themoonis.hypixel.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

@Data
@AllArgsConstructor
public class WorldPoint {

    private double x, y, z,yaw,pitch;

    public WorldPoint(double x,double y,double z){
        this(x,y,z,0,0);
    }

    public WorldPoint() {
        this(0, 0, 0,0,0);
    }

    public WorldPoint add(WorldPoint worldPoint) {
        this.x += worldPoint.x;
        this.y += worldPoint.y;
        this.z += worldPoint.z;
        return this;
    }
    public WorldPoint subtract(WorldPoint worldPoint) {
        this.x -= worldPoint.x;
        this.y -= worldPoint.y;
        this.z -= worldPoint.z;
        return this;
    }
    public WorldPoint multiply(WorldPoint worldPoint) {
        this.x *= worldPoint.x;
        this.y *= worldPoint.y;
        this.z *= worldPoint.z;
        return this;

    }
    public WorldPoint divide(WorldPoint worldPoint) {
        this.x /= worldPoint.x;
        this.y /= worldPoint.y;
        this.z /= worldPoint.z;
        return this;
    }

    @Override
    public String toString(){
        return x + "," + y + "," + z;
    }
    public String toStringWithRotation(){
        return x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    public static WorldPoint fromBukkitLocation(Location location){
        return new WorldPoint(location.getX(),location.getY(),location.getZ(),location.getYaw(),location.getPitch());
    }
    public Location toBukkitPoint(World world){
        return new Location(world,x,y,z, (float) yaw, (float) pitch);
    }

    public static BlockFace getCardinalDirection(double rotation) {

        if (rotation < 0) {
            rotation += 360.0;
        }

        if (rotation >= 315 || rotation < 45) {
            return BlockFace.SOUTH;
        } else if (rotation >= 45 && rotation < 135) {
            return BlockFace.WEST;
        } else if (rotation >= 135 && rotation < 225) {
            return BlockFace.NORTH;
        } else if (rotation >= 225) {
            return BlockFace.EAST;
        }
        return BlockFace.NORTH;
    }

}
