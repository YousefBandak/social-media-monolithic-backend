package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;

import java.util.List;

public interface Strategy {

    public void operate(List<? extends Content> contentList);
}
