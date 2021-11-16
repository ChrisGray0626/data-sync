package pers.chris.core.reader;

import pers.chris.common.plugin.ResponseParsePluginable;
import pers.chris.core.filter.APIFilter;
import pers.chris.core.model.APIConf;

public interface APIReadable {

    void init(APIConf apiConf, APIFilter apiFilter, ResponseParsePluginable responseParsePluginable);

}
