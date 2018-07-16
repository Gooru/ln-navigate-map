package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/7/18.
 */
class Route0NextContentFinder implements ContentFinder {

    private final DBI dbi;
    private final ContentVerifier verifier;
    private PathFinderContext context;
    private Route0ContentFinderDao route0ContentFinderDao;
    private Route0ContentFinderContext route0ContentFinderContext;

    Route0NextContentFinder(DBI dbi, ContentVerifier verifier) {
        this.dbi = dbi;
        this.verifier = verifier;
    }

    @Override
    public ContentAddress findContent(PathFinderContext context) {
        this.context = context;
        initialize();
        return findNextVerifiedContent();
    }

    private ContentAddress findNextVerifiedContent() {
        List<UserRoute0ContentDetailModel> models;
        if (route0ContentFinderContext.getCollectionId() != null) {
            if (route0ContentFinderContext.getClassId() == null) {
                models = getRoute0ContentFinderDao().fetchNextContentsFromRoute0ForIL(route0ContentFinderContext);
            } else {
                models = getRoute0ContentFinderDao().fetchNextContentsFromRoute0InClass(route0ContentFinderContext);
            }
        } else {
            if (route0ContentFinderContext.getClassId() == null) {
                models = getRoute0ContentFinderDao().fetchAllContentsFromRoute0ForIL(route0ContentFinderContext);
            } else {
                models = getRoute0ContentFinderDao().fetchAllContentsFromRoute0InClass(route0ContentFinderContext);
            }
        }
        ContentAddress result;
        for (UserRoute0ContentDetailModel model : models) {
            result = model.asContentAddress(context.getContentAddress().getCourse());
            if (verifier.isContentVerified(result)) {
                return result;
            }
        }
        return null;
    }

    private void initialize() {
        route0ContentFinderContext = Route0ContentFinderContext.buildFromPathFinderContext(context);
    }

    private Route0ContentFinderDao getRoute0ContentFinderDao() {
        if (route0ContentFinderDao == null) {
            route0ContentFinderDao = dbi.onDemand(Route0ContentFinderDao.class);
        }
        return route0ContentFinderDao;
    }

}
