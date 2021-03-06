package com.yahoo.athenz.zts;
/**
 * Copyright 2016 Yahoo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;

public class ZTSDaemon implements Daemon {

    private String[] args = null;

    public void init(DaemonContext context) throws Exception {
        args = context.getArguments();
    }

    public void start() throws Exception {
        if (args == null) {
            return;
        }
        ZTS.main(args);
    }

    public void stop() throws Exception {
    }

    public void destroy() {
    }
}
