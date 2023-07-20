/*
 * Copyright 2009-2011 Jon Stevens et al.
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

package com.github.sardine.impl.handler;

import com.github.sardine.impl.SardineException;
import org.apache.http4.HttpResponse;
import org.apache.http4.HttpStatus;
import org.apache.http4.StatusLine;
import org.apache.http4.client.ResponseHandler;

/**
 * {@link ResponseHandler} which checks whether a given resource exists.
 *
 * @author mirko
 */
public class ExistsResponseHandler extends ValidatingResponseHandler<Boolean> {
    @Override
    public Boolean handleResponse(HttpResponse response) throws SardineException {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
            return true;
        }
        if (statusCode == HttpStatus.SC_NOT_FOUND || statusCode == HttpStatus.SC_FORBIDDEN) {
            return false;
        }
        throw new SardineException("Unexpected response", statusCode, statusLine.getReasonPhrase());
    }
}
