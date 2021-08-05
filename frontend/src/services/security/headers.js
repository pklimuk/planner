export default () => { 
    return {
      'authorization': localStorage.getItem('auth'),
    }
  }