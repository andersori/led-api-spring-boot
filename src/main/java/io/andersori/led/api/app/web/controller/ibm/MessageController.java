package io.andersori.led.api.app.web.controller.ibm;

import static io.andersori.led.api.app.web.controller.util.PathConfig.PUBLIC_PATH;
import static io.andersori.led.api.app.web.controller.util.PathConfig.VERSION;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.IBMService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(VERSION)
@AllArgsConstructor
public class MessageController {

	private final IBMService ibmService;

	@GetMapping(value = PUBLIC_PATH + "/message", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getAppUuid() {
		return UUID.randomUUID().toString();
	}

	@PostMapping(value = PUBLIC_PATH
			+ "/message", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getMesage(@RequestBody String message, @RequestParam(required = true) String appUuid) throws DomainException {
		MessageResponse msg = ibmService.getMessage(appUuid, message);
		return msg.getOutput().getGeneric().toString();
	}
}
