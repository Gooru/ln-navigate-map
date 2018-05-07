package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.ArrayList;
import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.SuggestedContentType;
import org.gooru.navigatemap.infra.data.Trackable;
import org.gooru.navigatemap.infra.data.Tracker;

/**
 * @author ashish on 7/5/18.
 */
class PathFinderResult implements Trackable {

    private ContentAddress contentAddress;
    private List<String> suggestions;
    private SuggestedContentType suggestedContentType;
    private List<Tracker> trackers;

    public ContentAddress getContentAddress() {
        return contentAddress;
    }

    public void setContentAddress(ContentAddress contentAddress) {
        this.contentAddress = contentAddress;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public SuggestedContentType getSuggestedContentType() {
        return suggestedContentType;
    }

    public void setSuggestedContentType(SuggestedContentType suggestedContentType) {
        this.suggestedContentType = suggestedContentType;
    }

    public PathFinderResult(ContentAddress contentAddress, List<String> suggestions,
        SuggestedContentType suggestedContentType) {
        this.contentAddress = contentAddress;
        this.suggestions = suggestions;
        this.suggestedContentType = suggestedContentType;
    }

    public PathFinderResult(List<String> suggestions, SuggestedContentType suggestedContentType) {
        this.suggestions = suggestions;
        this.suggestedContentType = suggestedContentType;
    }

    public PathFinderResult(ContentAddress contentAddress) {
        this.contentAddress = contentAddress;
    }

    public boolean hasSuggestions() {
        return suggestions != null && !suggestions.isEmpty();
    }

    @Override
    public List<Tracker> trackers() {
        return trackers;
    }

    public void addTracker(Tracker tracker) {
        if (trackers == null) {
            trackers = new ArrayList<>();
        }
        trackers.add(tracker);
    }
}
