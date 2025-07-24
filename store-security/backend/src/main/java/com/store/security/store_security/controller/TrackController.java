package com.store.security.store_security.controller;

import com.store.security.store_security.dto.TrackDto;
import com.store.security.store_security.service.ITrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "Track management",
		description = "REST API to manage track"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/track")
public class TrackController {

	private final ITrackService	trackService;

	@Operation(
			summary = "Get track by order",
			description = "REST API to get track by order"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_TRACK')")
	@GetMapping("/{idOrder}")
	public ResponseEntity<TrackDto> getTrackByOrder(@PathVariable("idOrder") Long idOrder) {
		return ResponseEntity.ok(trackService.getTrackByOrder(idOrder));
	}

	@Operation(
			summary = "Set track by order",
			description = "REST API to set track by order"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRACK')")
	@PutMapping("/{idOrder}")
	public ResponseEntity<TrackDto> setTrack(@PathVariable("idOrder") Long idOrder,@RequestBody TrackDto trackDto) {
		return ResponseEntity.ok(trackService.setTrack(idOrder,trackDto));
	}
}
