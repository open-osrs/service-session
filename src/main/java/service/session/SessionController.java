/*
 * Copyright (c) 2020, Null <TheRealNull@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package service.session;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController
{
	public static HashMap<String, Session> activeSessions = new HashMap<>();

	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void removeExpiredSessions()
	{
		HashMap<String, Session> newSessions = new HashMap<>();
		for (Map.Entry<String, Session> session : activeSessions.entrySet())
		{
			if (!(session.getValue().lastUpdate < (System.currentTimeMillis() - 11 * 60 * 1000)))
			{
				newSessions.put(session.getKey(), session.getValue());
			}
		}
		activeSessions = newSessions;
	}

	@RequestMapping("/new")
	public String newSession(HttpServletRequest request)
	{
		Session newSession = new Session(UUID.randomUUID());
		activeSessions.put(request.getRemoteAddr(), newSession);
		return newSession.uuid.toString();
	}

	@RequestMapping("/ping")
	public void pingSession(HttpServletRequest request, @RequestParam String uuid)
	{
		Session newSession = new Session(UUID.fromString(uuid));
		activeSessions.put(request.getRemoteAddr(), newSession);
	}

	@RequestMapping("/count")
	public int count()
	{
		return activeSessions.values().size();
	}
}