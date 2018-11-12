import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import {IconButton, GridListTileBar, GridListTile, GridList } from "@material-ui/core/";
import {ThumbUp, AddCircleOutlined} from "@material-ui/icons";
import Snackbar from '@material-ui/core/Snackbar';

const styles = {
            gridList: {
                flexWrap: 'nowrap',
                backgroundColor: 'black',
            },
            gridListCreate: {
                flexWrap: 'flex',
                backgroundColor: 'white',
                width: 'webkit-fill-available',
                maxWidth: '100%',
                justifyContent:'space-around',
            },
            gridListTile: {
                marginTop: '30px',
                height: 'webkit-fill-available',
                width: 'webkit-fill-available',
                maxHeight: '100px',
                maxWidth: '100px',
                boxShadow: '0px 2px 4px -1px rgba(0, 0, 0, 0.2),0px 4px 5px 0px rgba(0, 0, 0, 0.14),0px 1px 10px 0px rgba(0, 0, 0, 0.12)',
            },
            gridListTileCreate: {
                marginTop: '30px',
                height: 'webkit-fill-available',
                width: 'webkit-fill-available',
                maxHeight: '100px',
                maxWidth: '100px',
                boxShadow: '0px 2px 4px -1px rgba(0, 0, 0, 0.2),0px 4px 5px 0px rgba(0, 0, 0, 0.14),0px 1px 10px 0px rgba(0, 0, 0, 0.12)',
                '&:hover': {
                    backgroundColor: '#0303031a !important',
                    opacity: '0.75',
                },
            },
            titleBar: {
                background: 'linear-gradient(to top, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.3) 70%, rgba(0,0,0,0) 100%)',
            },
            button: {
                zIndex: '500',
            },
            image: {
                // borderRadius: '100%',

            },
            name: {
                bottom: '10px'
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
            openSnackbar: props.snackbar || false,
            verticalSnackbar: 'top',
            horizontalSnackbar: 'center',
        };
    };

    componentWillReceiveProps(newProps) {
        if(newProps.delete){
            this.delete;
        };
    };

    handleClick = state => () => {
        this.setState({ openSnackbar: true, ...state });
    };

    handleClose = () => {
        this.setState({ openSnackbar: false });
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

onHover = () => {
    console.log('läuft rein');
    this.setState({
        openSnackbar: true,
    })
};

offHover = () => {
    console.log('läuft raus');
    this.setState({
        openSnackbar: false,
    })
};


    render(){
        const {classes} = this.props;
        let picData = speedSelectArray;
        let verticalSnackbar = this.state.verticalSnackbar;
        let horizontalSnackbar = this.state.horizontalSnackbar;
        let openSnackbar = this.state.openSnackbar;
        console.log('render', openSnackbar);

            if(this.state.create){
               return(

                <GridList cellHeight={100} className={classes.gridListCreate} cols={2}>
                    {picData.map(tile => (

                        <GridListTile onMouseEnter={this.onHover} onMouseLeave={this.offHover} className={classes.gridListTileCreate} key={tile.name} onClick={() => this.selectPhoto(tile, true)} cols={tile.cols || 1} >

                            <img src={tile.picUrl} alt={tile.name} className={classes.image}/>
                            <GridListTileBar
                                title={'Add Event'}
                                classes={{
                                    root: classes.titleBar,
                                    title: classes.name
                                }}
                                actionIcon={
                                    (this.openSnackbar)

                                       ?    <IconButton style={tile.style} className={classes.button}>
                                                <AddCircleOutlined/>
                                            </IconButton>

                                        :   ""
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
export default  withStyles(styles)(SpeedSelectGrid);