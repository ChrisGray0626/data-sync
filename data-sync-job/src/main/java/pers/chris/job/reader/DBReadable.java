package pers.chris.job.reader;

import pers.chris.job.filter.DBFilter;
import pers.chris.common.model.DBConfBO;

public interface DBReadable {

    void init(DBConfBO dbConf, DBFilter dbFilter);

}
