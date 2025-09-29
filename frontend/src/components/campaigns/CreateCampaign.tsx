const CreateCampaign = () => {
  return (
    <div
      className="
    bg-gray-300
    m-10
    rounded-md
    p-5
    flex
    flex-col
    text-center
    justify-center
    items-center
    gap-5
    "
    >
      <h2
        className="
      font-semibold
      text-2xl"
      >
        New Campaign
      </h2>
      <div className="flex">
        <h3 className="text-lg">Campaign name:</h3>
        <input className="bg-gray-200 rounded-md border-1 mx-2" type="text" />
      </div>
      <div className="flex">
        <h3 className="text-lg">Wanted themes:</h3>
        <input className="bg-gray-200 rounded-md border-1 mx-2" type="text" />
      </div>
      <div className="flex">
        <h3 className="text-lg">Unwanted themes:</h3>
        <input className="bg-gray-200 rounded-md border-1 mx-2" type="text" />
      </div>
    </div>
  );
};

export default CreateCampaign;
