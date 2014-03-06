package hora;

public enum Mode {

    ON_THE_HOUR, EVERY_HOUR;

    @Override
    public String toString() {
        char[] name = name().toCharArray();
        for (int i = 0; i < name.length; i++) {
            if (name[i] == '_') {
                name[i] = ' ';
            } else if (i != 0 && name[i - 1] != ' ') {
                name[i] = Character.toLowerCase(name[i]);
            }
        }
        return new String(name);
    }

}
