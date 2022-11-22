/**
 * 
 */
package com.strandls.activity.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
import com.strandls.activity.pojo.PageAcitvityLogging;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.TaxonomyActivityLogging;
import com.strandls.activity.pojo.UserGroupActivityLogging;
import com.strandls.activity.service.ActivityService;
import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Abhishek Rudra
 *
 */

@Api("Activity Serivce")
@Path(ApiConstants.V1 + ApiConstants.SERVICE)
public class ActivityController {

	@Inject
	private ActivityService service;

	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping() {
		return Response.status(Status.OK).entity("PONG").build();
	}

	@GET
	@Path(ApiConstants.IBP + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find activity by ID for IBP", notes = "Returns activity details", response = ActivityResult.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Traits not found", response = String.class) })

	public Response getIbpActivity(@PathParam("objectType") String objectType, @PathParam("objectId") String objectId,
			@DefaultValue(value = "0") @QueryParam("offset") String offset,
			@DefaultValue(value = "10") @QueryParam("limit") String limit) {
		try {
			Long id = Long.parseLong(objectId);
			ActivityResult activityResult = service.fetchActivityIbp(objectType, id, offset, limit);
			return Response.status(Status.OK).entity(activityResult).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Logs activity", notes = "Return the activity", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to post the data", response = String.class) })

	public Response logActivity(@Context HttpServletRequest request,
			@ApiParam(name = "activityLogging") ActivityLoggingData activityLogging) {

		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logActivities(request, userId, activityLogging);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.SENDMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser

	@ApiOperation(value = "sents out cumulative mail and notification for observation create", notes = "sents out mail and notification for observationCreate", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to send the mail", response = String.class) })

	public Response sendMailCreateObservation(@Context HttpServletRequest request,
			@ApiParam(name = "activityLogging") ActivityLoggingData activityLogging) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			String result = service.sendObvCreateMail(userId, activityLogging);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.COMMENT + "/{commentType}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Adds a comment", notes = "Return the current activity", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to log a comment", response = String.class) })

	public Response addComment(@Context HttpServletRequest request,
			@ApiParam(name = "commentData") CommentLoggingData commentData,
			@PathParam("commentType") String commentType) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			if (commentData.getBody().trim().length() > 0) {
				Activity result = service.addComment(request, userId, commentType, commentData);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("Blank Comment Not allowed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.USERGROUP)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "logs userGroup Activity", notes = "Retruns the activity that is logged", response = Activity.class)
	@ApiResponses(value = {

			@ApiResponse(code = 400, message = "Unable to log the activity", response = String.class) })

	public Response logUserGroupActivity(@Context HttpServletRequest request,
			@ApiParam(name = "userGroupActivityLogging") UserGroupActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logUGActivities(userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.DOCUMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log document activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logDocumentActivity(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") DocumentActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logDocActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.PAGE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log page activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logPageActivity(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") PageAcitvityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logPageActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.SPECIES)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log species activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logSpeciesActivities(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") SpeciesActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logSpeciesActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.TAXONOMY)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log Taxonomy activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logTaxonomyActivities(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") TaxonomyActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logTaxonomyActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.CCA)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log cca activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logCCAActivities(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") CCAActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logCCAActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOG + ApiConstants.DATATABLE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "log DataTable activities", notes = "Return the activity logged", response = Activity.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to log the activity", response = String.class) })

	public Response logDatatableActivities(@Context HttpServletRequest request,
			@ApiParam(name = "loggingData") DatatableActivityLogging loggingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Activity result = service.logDatatableActivities(request, userId, loggingData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.COUNT + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Get activity count per objectid", notes = "Returns the activity count for the object", response = Integer.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to get the count", response = String.class) })

	public Response getActivityCount(@PathParam("objectType") String objectType,
			@PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			Integer result = service.activityCount(objectType, id);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CCA + ApiConstants.REQUESTMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Get activity count per objectid", notes = "Returns the activity count for the object", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to get the count", response = String.class) })

	public Response ccaMailRequest(@Context HttpServletRequest request,
			@ApiParam(name = "permissionReq") CcaPermission permissionReq) {

		try {
			if (!permissionReq.getEncryptKey().isEmpty() || permissionReq.getEncryptKey() != null) {
				Boolean result = service.checkCCARequest(permissionReq);
				return Response.status(Status.OK).entity(result).build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return null;

	}

}
