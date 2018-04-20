package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.database.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/location")
public class LocationController {

    private LocationLogic locationLogic;

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String createLocation(@RequestBody LocationJson location, HttpServletResponse response) {

        try {
            int locationId = locationLogic.createLocation("dummy",
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
            Location location = locationLogic.getLocation("dummy", locationId);

            if(location == null)
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            else
                return location;

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }

        return null;
    }

    @Autowired
    public void setLocationLogic(LocationLogic locationLogic) {
        this.locationLogic = locationLogic;
    }
}
