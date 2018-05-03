package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.database.Location;
import group.greenbyte.lunchplanner.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private LocationLogic locationLogic;

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String createLocation(@RequestBody LocationJson location, HttpServletResponse response) {

        try {
            int locationId = locationLogic.createLocation(SessionManager.getUserName(),
                    location.getLocationName(),location.getxCoordinate(),location.getyCoordinate(),location.getDescription());

            response.setStatus(HttpServletResponse.SC_CREATED);
            return String.valueOf(locationId);
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
    }

    @RequestMapping(value = "/{locationId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Location getLocation(@PathVariable int locationId, HttpServletResponse response) {

        try {
            Location location = locationLogic.getLocation(SessionManager.getUserName(), locationId);

            if(location == null)
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            else
                return location;

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }

        return null;
    }

    @RequestMapping(value = "/search/{word}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity searchLocation(@PathVariable String word) {
        try {
            List<Location> locations = locationLogic.searchLocation(SessionManager.getUserName(), word);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(locations);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    @Autowired
    public void setLocationLogic(LocationLogic locationLogic) {
        this.locationLogic = locationLogic;
    }
}
