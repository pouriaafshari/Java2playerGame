import java.net.URL;

public class Character {
    private final String name;
    private final String imagePath;

    public Character(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        if (imagePath == null || imagePath.isEmpty()) {
            return getDefaultImagePath();
        } else if (resourceExists(imagePath)) {
            return imagePath;
        } else {
            logImageNotFoundError();
            return getDefaultImagePath();
        }
    }

    private String getDefaultImagePath() {
        return "/CharacterImages/placeholder.jpg";
    }

    private boolean resourceExists(String path) {
        URL resourceUrl = getClass().getResource(path);
        return resourceUrl != null;
    }

    private void logImageNotFoundError() {
        System.err.println("Image resource not found for character: " + name);
    }
}
