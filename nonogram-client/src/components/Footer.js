

function Footer(props){

    
    


    return(

        <div className="Footer">
            <div className="Footer__stat">Your games: {props.gamesCount}</div>
            <div className="Footer__username">@ {props.username}</div>
            <div className="Footer__stat">Online: 1</div>
        </div>
    );

}

export default Footer;