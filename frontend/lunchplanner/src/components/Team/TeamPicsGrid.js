import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import {GridList, GridListTile, GridListTileBar, IconButton, Button, Modal} from '@material-ui/core';
import {ThumbUp, AddCircleOutlined, Close} from '@material-ui/icons';
import Dialog from "../Dialog";
import FloatingActionButton from "../FloatingActionButton";
import {Link} from "react-router-dom";
import {getHistory} from "../../utils/HistoryUtils";
import spacing from "@material-ui/core/es/styles/spacing";
import {light as palette} from "@material-ui/core";



const styles = {
    root: {
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-around',
        backgroundColor: 'black',
        overflow: 'hidden',

    },
    gridList: {
        flexWrap: 'nowrap',
        backgroundColor: 'black',

    },
    tile:{
        height: '20px',
    },
    subheader: {
        width: '100%',
    },
    button: {
        display: 'flex',
        position: 'absolute',
        left: '90%',
        backgroundColor: '#75a045',
        justifyContent: 'center',
    },
    buttonClose:{
        left: '0%',
        display: 'flex',
        position: 'absolute',
        justifyContent: 'center',
        backgroundColor: '#75a045',
        zIndex: '500',
        top: '0',

    },
    text: {
        fontSize: '16px',
        fontFamily: 'Work Sans',
        color: "white",
    },
    paper: {
        position: 'absolute',
        width: '50%',
        height:'50%'
        // backgroundColor: palette.background.paper,
        // padding:'100%',
    },

};

 let picData = [
            {
                number: 1,
                img: '/pics/gym.jpg',
                title: 'gym',
                author: 'Can Arsoy',
                cols: 1,
                style: {
                    color: 'white',
                },
            },
            {
                number: 2,
                img: '/pics/bike.jpg',
                title: 'bike',
                author: 'Can Arsoy',
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                number: 3,
                img: '/pics/china.jpg',
                title: 'chinese food',
                author: 'Can Arsoy',
                cols: 3,
                style: {
                    color: 'white',
                },
            },
            {
                number: 4,
                img: '/pics/code.jpg',
                title: 'code',
                author: 'Can Arsoy',
                cols: 1,style: {
                    color: 'white',
                },
            },
            {
                number: 5,
                img: '/pics/doener.jpg',
                title: 'doener',
                author: 'Can Arsoy',
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                number: 6,
                img: '/pics/pasta.jpg',
                title: 'pasta',
                author: 'Can Arsoy',
                cols: 3,
                style: {
                    color: 'white',
                },
            },
            {
                number: 7,
                img: '/pics/picnic.jpg',
                title: 'outside',
                author: 'Can Arsoy',
                cols: 1,
                style: {
                    color: 'white',
                },
            },
            {
                number: 8,
                img: '/pics/pizza.jpg',
                title: 'pizza',
                author: 'Can Arsoy',
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                number: 9,
                img: '/pics/stadium.jpg',
                title: 'stadium',
                author: 'Can Arsoy',
                cols: 3,
                style: {
                    color: 'white',
                },
            },
            {
                number: 10,
                img: '/pics/sushi.jpg',
                title: 'sushi',
                author: 'Can Arsoy',
                cols: 1,
                style: {
                    color: 'white',
                },
            },
            {
                number: 11,
                img: '/pics/tickets.jpg',
                title: 'event',
                author: 'Can Arsoy',
                cols: 2,
                style: {
                    color: 'white',
                },
            },
            {
                number: 12,
                img: '/pics/ux.jpg',
                title: 'UX',
                author: 'Can Arsoy',
                cols: 3,
                style: {
                    color: 'white',
                },
            },
            {
                number: 13,
                img: '/pics/vege.jpg',
                title: 'veggie',
                author: 'Can Arsoy',
                cols: 1,
                style: {
                    color: 'white',
                },
            },

           ];

class TeamPicsGrid extends React.Component {

    constructor(props) {
        super();

        this.state = {
            title: "",
            clicked: false,
            pic: "",
            open: props.open,
            picChange: props.picChange
        };


        this.handleSend = this.handleSend.bind(this);
    };


    handleSend = () => {
        let pic = this.state.pic;
        this.props.onChange(pic);
        this.handleClose()
    };
    handleOpen = () => {
        this.setState({
            open: true,
        });
    };

    handleClose = () => {
        this.setState({
            open: false,
        });
        this.props.handleClose();
    };

    onClose = () => {
            getHistory().goBack()

    };

    rand() {
        return Math.round(Math.random() * 20) - 10;
    }

    getModalStyle() {
        const top = 50 + this.rand();
        const left = 50 + this.rand();

        return {
            top: `${top}%`,
            left: `${left}%`,
            transform: `translate(-${top}%, -${left}%)`,
        };
    };

    selectPhoto = (event) => {
        let url = "";
        let numb = event.number;
        let color = event.style.color;
        let picData2 = {};

        if (this.state.clicked === false) {
            picData2 = picData.map(tile => {
                if (tile.number === numb) {
                    tile.style = {color: 'orange'};
                    url = tile.img;
                    this.props.onChange(url);
                }
            });

            this.setState({
                clicked: true,
                pic: url,
            });


        } else {
            picData2 = picData.map(tile => {
                if (tile.number === numb) {
                    if (tile.style.color === 'white') {
                        tile.style = {color: 'orange'};
                        url = tile.img;
                        this.setState({
                            clicked: true,
                            pic: url,
                        });
                        this.props.onChange(url);

                    } else {
                        tile.style = {color: 'white'};
                        this.setState({
                            clicked: false,
                        });
                        this.props.onChange("");
                    }
                } else {
                    tile.style = {color: 'white'}
                }
            });
        }


    };


    render() {
        const {classes} = this.props;
        console.log('picChange', this.state.picChange);

        if (this.state.picChange !== true) {
            return (
                <div className={classes.root}>
                    <GridList cellHeight={160} className={classes.gridList} cols={3}>
                        {picData.map(tile => (
                            <GridListTile key={tile.img} onClick={() => this.selectPhoto(tile)} cols={tile.cols || 1}>
                                <img src={tile.img} alt={tile.title}/>
                                <GridListTileBar
                                    title={tile.title}
                                    classes={{
                                        root: classes.titleBar,
                                        title: classes.title
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
                </div>
            )
        } else {
            return (
                <div className={classes.root}>
                    <div>
                        <IconButton className={classes.buttonClose} onClick={this.onClose}>
                            <Close />
                        </IconButton>
                    </div>
                    <GridList cellHeight={160} className={classes.gridList} cols={3}>
                        {picData.map(tile => (
                            <GridListTile className={classes.tile} ey={tile.img} onClick={() => this.selectPhoto(tile)} cols={tile.cols || 1}>
                                <img src={tile.img} alt={tile.title}/>
                                <GridListTileBar
                                    title={tile.title}
                                    classes={{
                                        root: classes.titleBar,
                                        title: classes.title
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
                    <div>
                        <IconButton className={classes.button} onClick={this.handleClose}>
                                  <AddCircleOutlined/>
                        </IconButton>
                    </div>
                </div>
            )
        }
    }
}
TeamPicsGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(TeamPicsGrid);
