/*
 * Copyright 2017 Hiroyuki Tamura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cks.hiroyuki2.worksupprotlib.RxMsg;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hiroyuki2 on 2018/02/09.
 */

public class RxMsgForUpdateComment {
    private String groupKey;
    private String contentsKey;
    private String newComment;

    public RxMsgForUpdateComment(@NonNull String groupKey, @NonNull String contentsKey, @Nullable String newComment) {
        this.groupKey = groupKey;
        this.contentsKey = contentsKey;
        this.newComment = newComment;
    }

    @NonNull
    public String getGroupKey() {
        return groupKey;
    }

    @NonNull
    public String getContentsKey() {
        return contentsKey;
    }

    @Nullable
    public String getNewComment() {
        return newComment;
    }
}
