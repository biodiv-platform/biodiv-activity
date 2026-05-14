package com.strandls.activity.controller;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.activity.ApiConstants;
import com.strandls.activity.pojo.Activity;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.ActivityResult;
import com.strandls.activity.pojo.CCAActivityLogging;
import com.strandls.activity.pojo.CcaPermission;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.DatatableActivityLogging;
import com.strandls.activity.pojo.DocumentActivityLogging;
import com.strandls.activity.pojo.ODKMailData;
import com.strandls.activity.pojo.PageAcitvityLogging;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.TaxonomyActivityLogging;
import com.strandls.activity.pojo.UserGroupActivityLogging;
import com.strandls.activity.service.ActivityService;
import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.minidev.json.JSONArray;

@Tag(name = "Activity Service", description = "APIs for Activity module")
@Path(ApiConstants.V1 + ApiConstants.SERVICE)
@Produces(MediaType.APPLICATION_JSON)
public class ActivityController {

	@Inject
	private ActivityService service;

	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)
	@Operation(summary = "Ping endpoint for health check", responses = {
			@ApiResponse(responseCode = "200", description = "PONG", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response ping() {
		return Response.status(Response.Status.OK).entity("PONG").build();
	}

	@GET
	@Path(ApiConstants.IBP + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Find activity by ID for IBP", responses = {
			@ApiResponse(responseCode = "200", description = "Activity details", content = @Content(schema = @Schema(implementation = ActivityResult.class))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "404", description = "Traits not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getIbpActivity(
			@Parameter(description = "Object Type", required = true) @PathParam("objectType") String objectType,
			@Parameter(description = "Object ID", required = true) @PathParam("objectId") String objectId,
			@Parameter(description = "Offset", example = "0") @DefaultValue("0") @QueryParam("offset") String offset,
			@Parameter(description = "Limit", example = "10") @DefaultValue("10") @QueryParam("limit") String limit) {
		try {
			Long id = Long.parseLong(objectId);
			ActivityResult activityResult = service.fetchActivityIbp(objectType, id, offset, limit);
			return Response.status(Response.Status.OK).entity(activityResult).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Logs activity", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ActivityLoggingData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to post the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logActivity(@Context HttpServletRequest request,
			@Parameter(description = "Activity logging data", required = true) ActivityLoggingData activityLogging) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logActivities(request, userId, activityLogging);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.SENDMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@ValidateUser
	@Operation(summary = "Sends out cumulative mail and notification for observation create", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ActivityLoggingData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Mail and notification sent", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send the mail", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response sendMailCreateObservation(@Context HttpServletRequest request,
			@Parameter(description = "Activity logging data", required = true) ActivityLoggingData activityLogging) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			String result = service.sendObvCreateMail(userId, activityLogging);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.COMMENT + "/{commentType}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Adds a comment", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CommentLoggingData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Comment logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "406", description = "Blank Comment Not Allowed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log a comment", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addComment(@Context HttpServletRequest request,
			@Parameter(description = "Comment data", required = true) CommentLoggingData commentData,
			@Parameter(description = "Comment type", required = true) @PathParam("commentType") String commentType) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			if (commentData.getBody().trim().length() > 0) {
				Activity result = service.addComment(request, userId, commentType, commentData);
				return Response.status(Response.Status.OK).entity(result).build();
			}
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Blank Comment Not allowed").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.DELETE + ApiConstants.COMMENT + "/{commentType}/{commentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Deletes (removes) a comment", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CommentLoggingData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Comment removed", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "406", description = "Blank Comment Not Allowed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to remove a comment", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response deleteComment(@Context HttpServletRequest request,
			@Parameter(description = "Comment data", required = true) CommentLoggingData commentData,
			@Parameter(description = "Comment type", required = true) @PathParam("commentType") String commentType,
			@Parameter(description = "Comment id", required = true) @PathParam("commentId") String commentId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			if (commentData.getBody().trim().length() > 0) {
				Activity result = service.removeComment(request, userId, commentType, commentData, commentId);
				return Response.status(Response.Status.OK).entity(result).build();
			}
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Blank Comment Not allowed").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.USERGROUP)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Logs userGroup Activity", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserGroupActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logUserGroupActivity(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup activity logging", required = true) UserGroupActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logUGActivities(userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.DOCUMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log document activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = DocumentActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logDocumentActivity(@Context HttpServletRequest request,
			@Parameter(description = "Document activity logging", required = true) DocumentActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logDocActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.PAGE)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log page activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = PageAcitvityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logPageActivity(@Context HttpServletRequest request,
			@Parameter(description = "Page activity logging", required = true) PageAcitvityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logPageActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.SPECIES)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log species activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SpeciesActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logSpeciesActivities(@Context HttpServletRequest request,
			@Parameter(description = "Species activity logging", required = true) SpeciesActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logSpeciesActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.TAXONOMY)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log Taxonomy activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TaxonomyActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logTaxonomyActivities(@Context HttpServletRequest request,
			@Parameter(description = "Taxonomy activity logging", required = true) TaxonomyActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logTaxonomyActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.CCA)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log CCA activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CCAActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logCCAActivities(@Context HttpServletRequest request,
			@Parameter(description = "CCA activity logging", required = true) CCAActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logCCAActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.DATATABLE)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Log DataTable activities", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = DatatableActivityLogging.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Activity logged", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to log the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response logDatatableActivities(@Context HttpServletRequest request,
			@Parameter(description = "DataTable activity logging", required = true) DatatableActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logDatatableActivities(request, userId, loggingData);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.COUNT + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Get activity count per objectid", responses = {
			@ApiResponse(responseCode = "200", description = "Activity count", content = @Content(schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "400", description = "Unable to get the count", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getActivityCount(
			@Parameter(description = "Object Type", required = true) @PathParam("objectType") String objectType,
			@Parameter(description = "Object ID", required = true) @PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			Integer result = service.activityCount(objectType, id);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CCA + ApiConstants.REQUESTMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Send permission request mail for CCA", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CcaPermission.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Permission request sent", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send cca permission request mail", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response ccaMailRequest(@Context HttpServletRequest request,
			@Parameter(description = "Permission request data", required = true) CcaPermission permissionReq) {
		try {
			Boolean result = service.checkCCARequest(permissionReq);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.CCA + ApiConstants.DOWNLOADMAIL + "/{fileName}/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Send download link mail for CCA", responses = {
			@ApiResponse(responseCode = "200", description = "Download link sent", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send cca download mail", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response ccaDownloadMail(@Context HttpServletRequest request,
			@Parameter(description = "File name", required = true) @PathParam("fileName") String fileName,
			@Parameter(description = "Type", required = true) @PathParam("type") String type) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			String userId = profile.getId();
			Boolean result = service.sendDownloadLink(userId, fileName, type);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.SPECIES + ApiConstants.DOWNLOADMAIL + "/{fileName}/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Send download link mail for species", responses = {
			@ApiResponse(responseCode = "200", description = "Download link sent", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send species download mail", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response speciesDownloadMail(@Context HttpServletRequest request,
			@Parameter(description = "File name", required = true) @PathParam("fileName") String fileName,
			@Parameter(description = "Type", required = true) @PathParam("type") String type) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			String userId = profile.getId();
			Boolean result = service.sendSpeciesDownloadLink(userId, fileName, type);
			return Response.status(Response.Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ODK + ApiConstants.SENDMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Send email to ODK users", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ODKMailData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "ODK mail sent", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send email to ODK users", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized") })
	public Response odkUserMail(@Context HttpServletRequest request,
			@Parameter(description = "ODK mail data", required = true) ODKMailData odkMailData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				Boolean result = service.odkUserMail(odkMailData);
				return Response.status(Response.Status.OK).entity(result).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.CROPCERT)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Save the cropcert activity", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Activity.class))), responses = {
			@ApiResponse(responseCode = "201", description = "Activity created", content = @Content(schema = @Schema(implementation = Activity.class))),
			@ApiResponse(responseCode = "400", description = "Unable to save the activity", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response save(@Context HttpServletRequest request,
			@Parameter(description = "Cropcert activity logging", required = true) Activity loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity activity = service.logCropcertActivities(request, userId, loggingData);
			return Response.status(Response.Status.CREATED).entity(activity).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
