import React from 'react';
import Dialog from "../Dialog";
import Tetris from 'react-tetris';

class MyTetris extends React.Component {

    render() {
        return(
            <Dialog>
                <Tetris>
                    {({
                          HeldPiece,
                          Gameboard,
                          PieceQueue,
                          points,
                          linesCleared
                      }) => {
                        // Render it however you'd like
                        return (
                            <div>
                                <HeldPiece />
                                <div>
                                    <p>Points: {points}</p>
                                    <p>Lines Cleared: {linesCleared}</p>
                                </div>
                                <Gameboard />
                                <PieceQueue />
                            </div>
                        )
                    }}
                </Tetris>
            </Dialog>
        )
    }
}

export default MyTetris;