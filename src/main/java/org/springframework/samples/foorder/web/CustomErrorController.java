/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.foorder.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController{
    
    @Override
    public String getErrorPath() {
        return "/error";
    }

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		// get error status
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
				int statusCode = Integer.parseInt(status.toString());
				// display specific error page
				if (statusCode == HttpStatus.NOT_FOUND.value()) {
					return "errors/error-404";
				} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
					return "errors/error-500";
				} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
					return "errors/error-403";
				}
		}
		// display generic error
		return "exception";
	}
	
}
