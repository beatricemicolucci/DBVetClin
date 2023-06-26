package utils;

import java.util.Objects;

public class ThreeKeys<X,Y,Z> {

    private final X x;
    private final Y y;
    private final Z z;

    public ThreeKeys(final X x, final Y y, final Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Z getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "ThreeKeys{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreeKeys<?, ?, ?> threeKeys = (ThreeKeys<?, ?, ?>) o;
        return Objects.equals(getX(), threeKeys.getX()) && Objects.equals(getY(), threeKeys.getY()) && Objects.equals(getZ(), threeKeys.getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
