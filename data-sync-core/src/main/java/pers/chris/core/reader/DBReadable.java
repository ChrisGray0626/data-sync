package pers.chris.core.reader;

import pers.chris.core.filter.DBFilter;
import pers.chris.core.model.DBConf;

public interface DBReadable {

    void init(DBConf dbConf, DBFilter dbFilter);

}
