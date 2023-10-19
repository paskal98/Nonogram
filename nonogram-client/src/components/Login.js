import { useState } from "react";



function Login(props){


    const onSubmit = (e)=>{
        e.preventDefault();
        props.formSubmit(e,props.data);
    }

    const onCreateUser = ()=>{
        props.onCreateUser(props.data);
    }

    return(

        <div>

            <form autoComplete='off' className='form' onSubmit={onSubmit}>
                <div className='control'>

                {props.status==="none" ? <h1 style={{color:"#fff"}}> Sign Up </h1> : <h1 style={{color:"#fff"}}> Sign In </h1>}
                    

                    {props.status==="wrong" && <div  style={{color:"#da5353"}}>Wrong password! </div>}
                    {props.status==="none" && <div  style={{color:"#8c8c8c"}}>Create new user?</div>}
                </div>
                <div className='control block-cube block-input'>
                    <input name='nickname' placeholder='nickname' type='text' value={props.data.nickname}  onChange={(e)=> props.setData({...props.data, nickname: e.target.value})}/>
                    <div className='bg-top'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg-right'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg'>
                    <div className='bg-inner'></div>
                    </div>
                </div>
                <div className='control block-cube block-input'>
                    <input name='password' placeholder='Password' type='password' value={props.data.password}  onChange={(e)=> props.setData({...props.data, password: e.target.value})}/>
                    <div className='bg-top'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg-right'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg'>
                    <div className='bg-inner'></div>
                    </div>
                </div>
                <button className='btn block-cube block-cube-hover' type='submit'>
                    <div className='bg-top'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg-right'>
                    <div className='bg-inner'></div>
                    </div>
                    <div className='bg'>
                    <div className='bg-inner'></div>
                    </div>

                    {props.status==="none" ?  <div onClick={onCreateUser} className='text'>Create</div> : <div className='text'>Log In</div>}
                    

                </button>
                <div className='credits'>
                    <a href='https://codepen.io/marko-zub/' target='_blank'>
                    Nonogram
                    </a>
                </div>
            </form>

        </div>

    );

}

export default Login;