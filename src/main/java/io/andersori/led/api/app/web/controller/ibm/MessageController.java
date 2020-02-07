package io.andersori.led.api.app.web.controller.ibm;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.andersori.led.api.domain.service.IBMService;

import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.andersori.led.api.app.web.controller.util.PathConfig.PUBLIC_PATH;

@RestController
@RequestMapping(VERSION)
public class MessageController {

	private final IBMService ibmService;

	public MessageController(IBMService ibmService) {
		this.ibmService = ibmService;
	}

	@PostMapping(value = PUBLIC_PATH
			+ "/message", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getMesage(@RequestBody String message, HttpServletResponse res, HttpServletRequest req,
			@CookieValue(name = "APP_UUID", required = false) String appUuid) {
		if (appUuid == null) {
			appUuid = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("APP_UUID", appUuid);
			cookie.setDomain(req.getContextPath());
			cookie.setHttpOnly(true);
			res.addCookie(cookie);
		}
		return ibmService.getMessage(appUuid, message).getOutput().getGeneric().toString();
	}
}
