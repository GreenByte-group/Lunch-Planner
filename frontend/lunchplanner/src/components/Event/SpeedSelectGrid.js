import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import {IconButton, GridListTileBar, GridListTile, GridList } from "@material-ui/core/";
import {ThumbUp} from "@material-ui/icons";

const styles = {
            gridList: {
                flexWrap: 'nowrap',
                backgroundColor: 'black',
            },
};

const speedSelectArray = [
            {
                name: "die Metzgerei",
                picUrl: "/pics/metzgerei.jpg",
                cols: 1,
                style: {
                    color: 'white',
                },
            },
            {
                name: "Dean and David",
                picUrl: "/pics/D&D.jpg",
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                name: "Döner",
                picUrl: "/pics/doener.jpg",
                cols: 1,
                style: {
                    color: 'white',
                },
            },
            {
                name: "Pizza Paradiso",
                picUrl: "/pics/pizza.jpg",
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                name: "Asiahung",
                picUrl: "/pics/asiahung.jpg",
                cols: 1,
                style: {
                    color: 'white',
                },
            }
            ];


class SpeedSelectGrid extends React.Component {

    constructor(props){
        super();
        this.state = {
            select: props.select || "",
            open: props.open || false,
            onChange: props.onChange,
            clicked: false,
            delete: props.delete,
            create: props.create || false,
        };
    };

    componentWillReceiveProps(newProps) {
        if(newProps.delete){
            this.delete;
        };
    };

    selectPhoto = (event, buttonStyle) => {
        let url = "";
        let button = buttonStyle;
        let currentName = event.name;
        let color = event.style.color;
        let picData2 = {};

        if (this.state.clicked === false) {
            picData2 = speedSelectArray.map(tile => {
                if (tile.name === currentName) {
                    if(!button) {
                        tile.style = {color: 'orange'};
                    }
                    url = tile.name;
                    this.props.onChange(url);
                }
            });
            this.setState({
                clicked: true,
                picUrl: url,
            });
        } else {
            picData2 = speedSelectArray.map(tile => {
                if (tile.name === currentName) {
                    if (tile.style.color === 'white') {
                        if(!button) {
                            tile.style = {color: 'orange'};
                        };
                        url = tile.name;
                        this.setState({
                            clicked: true,
                            picUrl: url,
                        });
                        this.props.onChange(url);

                    } else {
                        if(!button) {
                            tile.style = {color: 'white'};
                        }
                        this.setState({
                            clicked: false,
                        });
                        this.props.onChange("");
                    }
                } else {
                    if(!button) {
                        tile.style = {color: 'white'};
                    }
                }
            });
        }
    };


delete  = () => {
    speedSelectArray.map(tile => {tile.style = {color: 'white'};});


    this.setState({
         clicked: false,
    });
};


    render(){
        const {classes} = this.props;
        let picData = speedSelectArray;


            if(this.state.create){
               return(
                <GridList cellHeight={160} className={classes.gridList} cols={2}>
                    {picData.map(tile => (
                        <GridListTile key={tile.name} onClick={() => this.selectPhoto(tile, true)} cols={tile.cols || 1}>
                            <img src={tile.picUrl} alt={tile.name}/>
                            <GridListTileBar
                                title={tile.name}
                                classes={{
                                    root: classes.titleBar,
                                    title: classes.name
                                }}
                                actionIcon={
                                    <IconButton style={tile.style}>
                                        <p>ADD EVENT</p>
                                    </IconButton>
                                }
                            />
                        </GridListTile>
                    ))}
                </GridList>);
            }else{
                return(
                <GridList cellHeight={160} className={classes.gridList} cols={2}>
                    {picData.map(tile => (
                        <GridListTile key={tile.name} onClick={() => this.selectPhoto(tile, false)} cols={tile.cols || 1}>
                            <img src={tile.picUrl} alt={tile.name}/>
                            <GridListTileBar
                                title={tile.name}
                                classes={{
                                    root: classes.titleBar,
                                    title: classes.name
                                }}
                                actionIcon={
                                    <IconButton style={tile.style}>
                                        <ThumbUp/>
                                    </IconButton>
                                }
                            />
                        </GridListTile>
                    ))}
                </GridList>
                );
            }


    }

}
SpeedSelectGrid.propTypes = {
    classes: PropTypes.object.isRequired,
}
export default withStyles(styles)(SpeedSelectGrid);