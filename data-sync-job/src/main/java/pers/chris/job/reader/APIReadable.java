package pers.chris.job.reader;

import pers.chris.common.plugin.ResponseParsePluginable;
import pers.chris.job.filter.APIFilter;
import pers.chris.common.model.APIConfBO;

public interface APIReadable {

    void init(APIConfBO apiConf, APIFilter apiFilter, ResponseParsePluginable responseParsePluginable);

}
