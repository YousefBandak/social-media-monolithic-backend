package object_orienters.techspot.feed;

import lombok.Getter;
import object_orienters.techspot.content.Content;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Strategy {

    List<Content> contentList;

    Strategy() {
        contentList = new ArrayList<>();
    }

    public abstract void operate();
}
